    private void zip(File input, File output) {
        OutputStream os = null;
        InputStream is = null;
        try {
            os = new GZIPOutputStream(new FileOutputStream(output));
            is = new FileInputStream(input);
            byte[] buffer = new byte[8192];
            for (int length; (length = is.read(buffer)) != -1; ) os.write(buffer, 0, length);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
