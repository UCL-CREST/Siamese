    private static byte[] tryLoadFile(String path) throws IOException {
        InputStream in = new FileInputStream(path);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOUtils.copy(in, out);
        in.close();
        out.close();
        return out.toByteArray();
    }
