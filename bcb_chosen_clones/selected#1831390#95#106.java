    public byte[] readTsdFromFile(String filePath) throws IOException {
        System.out.println("reading TSD from file: " + filePath);
        FileInputStream fis = new FileInputStream(filePath);
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int bytesRead = 0;
        while ((bytesRead = fis.read(buffer, 0, buffer.length)) >= 0) {
            baos.write(buffer, 0, bytesRead);
        }
        fis.close();
        return baos.toByteArray();
    }
