    private byte[] readFile(File receta2) throws IOException {
        byte[] data = null;
        InputStream in = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            in = new FileInputStream(receta2);
            int read = 0;
            while ((read = in.read()) != -1) {
                baos.write((byte) read);
            }
            data = baos.toByteArray();
        } finally {
            try {
                in.close();
            } catch (Exception e2) {
            }
        }
        return data;
    }
