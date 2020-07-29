    public static byte[] getBytes(File file) throws IOException {
        if (file == null || !file.exists()) {
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        FileInputStream in = new FileInputStream(file);
        int c = in.read();
        while (c != -1) {
            out.write(c);
            c = in.read();
        }
        in.close();
        out.close();
        return out.toByteArray();
    }
