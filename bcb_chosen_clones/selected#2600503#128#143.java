    private static byte[] readBytes(File file) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            InputStream fis = new FileInputStream(file);
            InputStream is = new BufferedInputStream(fis);
            int count;
            byte[] buf = new byte[16384];
            while ((count = is.read(buf)) != -1) {
                if (count > 0) baos.write(buf, 0, count);
            }
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }
