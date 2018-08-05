    private static byte[] getBytes(File file) throws IOException {
        FileInputStream in = new FileInputStream(file);
        try {
            return toByteArray(in);
        } finally {
            in.close();
        }
    }
