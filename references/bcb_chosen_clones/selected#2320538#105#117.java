    private byte[] loadClass(File clFile) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream in = new FileInputStream(clFile);
        byte[] buffer = new byte[4096];
        int no = 0;
        try {
            while ((no = in.read(buffer)) != -1) out.write(buffer, 0, no);
        } finally {
            in.close();
            out.close();
        }
        return out.toByteArray();
    }
