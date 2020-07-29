    public static byte[] readFullFile(File file) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            byte[] buffer = new byte[8192];
            int read = 0;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            return out.toByteArray();
        } finally {
            if (in != null) in.close();
        }
    }
