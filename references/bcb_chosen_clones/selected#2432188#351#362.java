    private byte[] readFiletoBuffer(String file) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        InputStream in = new FileInputStream(file);
        int len = 0;
        byte[] buf = new byte[1024];
        while ((len = in.read(buf)) > 0) {
            os.write(buf, 0, len);
        }
        in.close();
        os.close();
        return os.toByteArray();
    }
