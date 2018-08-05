    public static byte[] readFileContentAsBytes(File file) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        FileInputStream is = new FileInputStream(file);
        try {
            Utils.transferContent(is, bytes, null);
        } finally {
            is.close();
        }
        return bytes.toByteArray();
    }
