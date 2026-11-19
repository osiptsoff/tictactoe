package ru.sbp.osiptsoff.tictactoe.handler.codec.serialize;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

/**
 * Encodes/decodes objects with Protobuf.
 */
public class ProtobufSerializer implements Serializer {
    /**
     * Here we avoid reflection overhead.
     */
    private static final Map<Class<?>, Schema<?>> SCHEMAS = new ConcurrentHashMap<>();

    /**
     * Here we avoid allocation overhead.
     * Must be used in assumption that tasks cannot be preempted (which suits
     * netty event loop architecture).
     */
    private static final ThreadLocal<LinkedBuffer> BUFFER = ThreadLocal.withInitial(
        () -> LinkedBuffer.allocate(512)
    );

    private static <T> Schema<T> getSchema(Class<T> clazz) {
        Schema<T> schema = (Schema<T>) SCHEMAS.get(clazz);
        if (schema != null) {
            return schema;
        }

        schema = RuntimeSchema.getSchema(clazz);
        SCHEMAS.put(clazz, schema);
        return schema;
    }

    private static LinkedBuffer getBuffer() {
        return BUFFER.get();
    }

    @Override
    public <T> byte[] serialize(T obj) {
        LinkedBuffer buffer = getBuffer();
        Schema<T> schema = getSchema((Class<T>) obj.getClass());

        try {
            return ProtobufIOUtil.toByteArray(obj, schema, buffer);
        } finally {
            buffer.clear();
        }
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        Schema<T> schema = getSchema(clazz);
        T obj = schema.newMessage();
        ProtobufIOUtil.mergeFrom(bytes, obj, schema);

        return obj;
    }
}
