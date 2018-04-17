    @Override
    public byte[] read(String path) throws PersistenceException {
        InputStream reader = null;
        ByteArrayOutputStream sw = new ByteArrayOutputStream();
        try {
            reader = new FileInputStream(path);
            IOUtils.copy(reader, sw);
        } catch (Exception e) {
            LOGGER.error("fail to read file - " + path, e);
            throw new PersistenceException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    LOGGER.error("fail to close reader", e);
                }
            }
        }
        return sw.toByteArray();
    }
