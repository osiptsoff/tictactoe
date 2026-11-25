package ru.sbp.osiptsoff.tictactoe.processor;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

@SupportedAnnotationTypes(
    "ru.sbp.osiptsoff.tictactoe.handler.codec.frame.identifier.Transportable")
public class TransportableProcessor extends AbstractProcessor {
    private static final String TARGET_PACKAGE = "ru.sbp.osiptsoff.tictactoe.handler.codec.frame.identifier";
    private static final String GENERATED_CLASS_SHORT_NAME = "TransportableRegistrar";
    private static final String GENERATED_CLASS_NAME = TARGET_PACKAGE + "." + GENERATED_CLASS_SHORT_NAME;

    private final Map<String, String> discoveredClasses = new TreeMap<>();
    private boolean generated = false;

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (generated) {
            return true;
        }

        TypeElement annotationElement = processingEnv.getElementUtils()
                .getTypeElement("ru.sbp.osiptsoff.tictactoe.handler.codec.frame.identifier.Transportable");
        if (annotationElement == null) {
            return false;
        }

        for (Element element : roundEnv.getElementsAnnotatedWith(annotationElement)) {
            if (element instanceof TypeElement typeElement) {
                discoveredClasses.put(typeElement.getQualifiedName().toString(),
                        typeElement.getSimpleName().toString());
            }
        }

        // Generate only after the last round to ensure all classes are discovered.
        if (roundEnv.processingOver() && !generated) {
            try {
                generateRegistry();
                generated = true;
            } catch (IOException e) {
                processingEnv.getMessager().printMessage(
                        Diagnostic.Kind.ERROR,
                        "Failed to generate " + GENERATED_CLASS_NAME + ": " + e.getMessage()
                );
            }
        }
        return true;
    }

    private void generateRegistry() throws IOException {
        Filer filer = processingEnv.getFiler();
        JavaFileObject file = filer.createSourceFile(GENERATED_CLASS_NAME);

        try (Writer writer = file.openWriter()) {
            writer.write("package " + TARGET_PACKAGE + ";\n\n");
            writer.write("public final class " + GENERATED_CLASS_SHORT_NAME + " {\n");
            writer.write("    private " + GENERATED_CLASS_SHORT_NAME + "() {}\n\n");
            writer.write("    public static void registerAll() {\n");
            for (String qualifiedName : discoveredClasses.keySet()) {
                writer.write("        IdentifierRegistry.register(" + qualifiedName + ".class);\n");
            }
            writer.write("    }\n");
            writer.write("}\n");
        }
    }
}