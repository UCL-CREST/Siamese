    protected byte[] getData(String path) throws FileNotFoundException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileInputStream fis = new FileInputStream(path);
        int i = 0;
        while ((i = fis.read()) != -1) {
            baos.write(i);
        }
        fis.close();
        return baos.toByteArray();
    }
