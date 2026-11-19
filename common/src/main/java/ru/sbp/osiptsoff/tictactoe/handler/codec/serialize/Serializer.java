package ru.sbp.osiptsoff.tictactoe.handler.codec.serialize;

/**
 * Object responsible for encoding and decoding POJO.
 */
public interface Serializer {
    /**
     * Serialize object to byte array.
     * Generally, this does not proride framing functionality, 
     * as well as there will be no way to distinguish type
     * before complete deserialization.
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
