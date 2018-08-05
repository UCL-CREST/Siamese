    private static String addFile(File ff, ZipOutputStream zipOutPic, String filename) throws Exception {
        FileInputStream file = new FileInputStream(ff);
        String ext = getFileExt(ff.getName());
        if (!ext.equals("")) {
            ext = "." + ext;
        }
        int randomString = ((int) (Math.random() * 1000000));
        while (randomList.contains(randomString)) {
            randomString = ((int) (Math.random() * 1000000));
        }
        randomList.add(randomString);
        String newFilename = filename == null ? randomString + ext : filename;
        ZipEntry entryPic = new ZipEntry(newFilename);
        zipOutPic.putNextEntry(entryPic);
        byte[] buf = new byte[1024];
        int len;
        while ((len = file.read(buf)) > 0) {
            zipOutPic.write(buf, 0, len);
        }
        zipOutPic.closeEntry();
        return newFilename;
    }
