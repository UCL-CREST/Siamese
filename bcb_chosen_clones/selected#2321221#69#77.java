    protected void add(ExtZipEntry zipEntry, InputStream zipData) throws IOException, UnsupportedEncodingException {
        zipOS.putNextEntry(zipEntry);
        byte[] data = new byte[1024];
        int read = zipData.read(data);
        while (read != -1) {
            zipOS.writeBytes(data, 0, read);
            read = zipData.read(data);
        }
    }
