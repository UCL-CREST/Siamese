    private void saveFile(InputStream in, String fullPath) {
        try {
            File sysfile = new File(fullPath);
            if (!sysfile.exists()) {
                sysfile.createNewFile();
            }
            java.io.OutputStream out = new FileOutputStream(sysfile);
            org.apache.commons.io.IOUtils.copy(in, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
