    public void addFile(ZipOutputStream out, String filename) {
        try {
            byte[] buf = new byte[1024];
            String filePath = projHandler.getProjectPath() + File.separator + filename;
            File file = new File(filePath);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(filePath);
                out.putNextEntry(new ZipEntry(filename));
                int len;
                while ((len = fis.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                fis.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
