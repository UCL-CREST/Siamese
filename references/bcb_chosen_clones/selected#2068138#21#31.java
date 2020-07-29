    public Map<String, Object> analyse(InputStream stream, Node node) throws IOException {
        Map<String, Object> properties = new HashMap<String, Object>();
        CRC32 crc = new CRC32();
        int b = stream.read();
        while (b != -1) {
            crc.update(b);
            b = stream.read();
        }
        properties.put(KEY_CRC, crc.getValue());
        return properties;
    }
