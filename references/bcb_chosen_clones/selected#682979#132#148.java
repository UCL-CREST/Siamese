    private void createZipFile(List<File> filesToZip, String zipFilePath, String rootFolder) throws IOException {
        byte[] buf = new byte[1024];
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFilePath));
        String regex = "^" + Pattern.quote(rootFolder) + "[\\\\/]?";
        for (File file : filesToZip) {
            FileInputStream in = new FileInputStream(file.getAbsolutePath());
            String relativePath = file.getAbsolutePath().replaceAll(regex, "");
            out.putNextEntry(new ZipEntry(relativePath));
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.closeEntry();
            in.close();
        }
        out.close();
    }
