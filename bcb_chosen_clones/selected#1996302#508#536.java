    private static String encodeResponse(Class<?> responseClass, Object object, boolean wasThrown, int flags, SerializationPolicy serializationPolicy) throws SerializationException {
        if (object instanceof Throwable) {
            Logger.log((Throwable) object);
            String escapedStr = ServerSerializationStreamWriter.escapeString(((Throwable) object).getMessage());
            CRC32 crc = new CRC32();
            try {
                crc.update(SerializabilityUtil.getSerializedTypeName(object.getClass()).getBytes(SerializabilityUtil.DEFAULT_ENCODING));
                Class<?> superClass = object.getClass().getSuperclass();
                while (superClass != null) {
                    crc.update(SerializabilityUtil.getSerializedTypeName(superClass).getBytes(SerializabilityUtil.DEFAULT_ENCODING));
                    superClass = superClass.getSuperclass();
                    if (superClass.equals(Throwable.class)) {
                        break;
                    }
                }
            } catch (Throwable t) {
                return "//EX[2,1,[\"java.lang.Exception/1920171873\"," + escapedStr + "],0," + AbstractSerializationStream.SERIALIZATION_STREAM_VERSION + "]";
            }
            return "//EX[2,1,[\"" + object.getClass().getName() + "/" + crc.getValue() + "\"," + escapedStr + "],0," + AbstractSerializationStream.SERIALIZATION_STREAM_VERSION + "]";
        }
        ServerSerializationStreamWriter stream = new ServerSerializationStreamWriter(serializationPolicy);
        stream.setFlags(flags);
        stream.prepareToWrite();
        if (responseClass != void.class) {
            stream.serializeValue(object, responseClass);
        }
        String bufferStr = (wasThrown ? "//EX" : "//OK") + stream.toString();
        return bufferStr;
    }
