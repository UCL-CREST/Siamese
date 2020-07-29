    public static void copyFile(File sourceFile, File destFile) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(sourceFile);
            os = new FileOutputStream(destFile);
            IOUtils.copy(is, os);
        } finally {
            try {
                if (os != null) os.close();
            } catch (Exception e) {
            }
            try {
                if (is != null) is.close();
            } catch (Exception e) {
            }
        }
    }
