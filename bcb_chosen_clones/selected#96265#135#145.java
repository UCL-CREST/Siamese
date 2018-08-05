    private void addFileToArchive(ZipOutputStream reportArchive, String file) throws FileNotFoundException, IOException {
        FileInputStream asf = new FileInputStream(file);
        reportArchive.putNextEntry(new ZipEntry(file));
        byte[] buffer = new byte[18024];
        int len;
        while ((len = asf.read(buffer)) > 0) {
            reportArchive.write(buffer, 0, len);
        }
        asf.close();
        reportArchive.closeEntry();
    }
