    private byte[] getFileContents(String fileName) throws Exception {
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        FileInputStream fis = new FileInputStream(fileName);
        byte[] data = new byte[1024];
        int read = fis.read(data, 0, 1024);
        while (read > -1) {
            ba.write(data, 0, read);
            read = fis.read(data, 0, 1024);
        }
        fis.close();
        return ba.toByteArray();
    }
