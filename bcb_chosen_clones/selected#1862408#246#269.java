    public static void createZipFile(String filename) throws IOException {
        if (filename == null || filename.length() < 1) {
            return;
        }
        String zipFileName = filename + ZIP_EXTENSION;
        ZipOutputStream zipStream = null;
        try {
            FileOutputStream outputStream = new FileOutputStream(zipFileName);
            zipStream = new ZipOutputStream(outputStream);
            StringBuffer content = FileUtil.getContent(filename);
            zipStream.setLevel(6);
            zipStream.putNextEntry(new ZipEntry(filename));
            zipStream.write(content.toString().getBytes(), 0, content.length());
            zipStream.closeEntry();
            zipStream.close();
        } finally {
            if (zipStream != null) {
                try {
                    zipStream.close();
                } catch (IOException e) {
                }
            }
        }
    }
