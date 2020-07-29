    private static byte[] readFile(File file) throws IOException {
        byte[] content = null;
        if (file != null) {
            InputStream in = null;
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                in = new FileInputStream(file);
                byte[] buf = new byte[BUFFER_SIZE];
                int n;
                while ((n = in.read(buf)) > 0) out.write(buf, 0, n);
                content = out.toByteArray();
            } finally {
                if (in != null) try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        return content;
    }
