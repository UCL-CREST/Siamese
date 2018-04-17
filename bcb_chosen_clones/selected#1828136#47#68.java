    public boolean putRecord(Serializable domain, Serializable table, Serializable key, Record record) throws StorageException {
        try {
            String recordPath = createDataPath(domain, table, key);
            boolean succeded = new File(recordPath).mkdirs();
            String recordFile = Commons.glue(recordPath, "/", key, ".zip");
            LOG.debug(recordFile);
            ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(new File(recordFile)));
            try {
                for (Map.Entry<String, Value> entry : record.entrySet()) {
                    zip.putNextEntry(new ZipEntry(entry.getKey()));
                    ObjectOutputStream os = new ObjectOutputStream(zip);
                    os.writeObject(entry.getValue());
                    zip.closeEntry();
                }
            } finally {
                zip.close();
            }
        } catch (IOException e) {
            throw new StorageException(e);
        }
        return true;
    }
