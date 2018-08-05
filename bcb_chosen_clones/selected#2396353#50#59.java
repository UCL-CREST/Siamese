    public static byte[] readFile(String inputFileName) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream in = new FileInputStream(inputFileName);
        try {
            copy(in, out);
        } finally {
            in.close();
        }
        return out.toByteArray();
    }
