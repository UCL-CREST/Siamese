    public static byte[] readBytes(File file) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            InputStream is = new BufferedInputStream(new FileInputStream(file));
            int count = 0;
            byte[] buf = new byte[16384];
            while ((count = is.read(buf)) != -1) {
                if (count > 0) {
                    baos.write(buf, 0, count);
                }
            }
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }
