package ru.sbp.osiptsoff.tictactoe.handler.codec.serialize;

/**
 * Object responsible for encoding and decoding POJO for wire.
 */
public interface Serializer {
    /**
     * Serialize object to byte array.
     * 
     * @param <T> object's type
     * 
     * @param obj object
     * 
     * @return bytes
     */
    <T> byte[] serialize(T obj);

    /**
     * Deserialize object from byte array.
     * 
     * @param <T> object's type
     * 
     * @param bytes bytes of object's data
     * @param clazz object's class
     * 
     * @return object
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz);
}
