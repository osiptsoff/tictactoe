package ru.sbp.osiptsoff.tictactoe.handler.codec.serialize;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * Encodes/decodes objects with Protobuf.
 */
public class ProtobufSerializer implements Serializer {
    /**
     * Here we avoid reflection overhead.
     */
    private static final Map<Class<?>, Schema<?>> SCHEMAS = new ConcurrentHashMap<>();

    @Getter(AccessLevel.PRIVATE)
    private final LinkedBuffer reusableBuffer = LinkedBuffer.allocate(512);

    private static <T> Schema<T> getSchema(Class<T> clazz) {
        Schema<T> schema = (Schema<T>) SCHEMAS.get(clazz);
        if (schema != null) {
            return schema;
        }

        schema = RuntimeSchema.getSchema(clazz);
        SCHEMAS.put(clazz, schema);
        return schema;
    }

    @Override
    public <T> byte[] serialize(T obj) {
        LinkedBuffer buffer = getReusableBuffer();
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
