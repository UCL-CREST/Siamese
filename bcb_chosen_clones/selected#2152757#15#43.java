    public static void zipDirectory(String dir, ZipOutputStream zos, String path) {
        try {
            File dirToZip = new File(dir);
            String[] dirList = dirToZip.list();
            byte[] readBuffer = new byte[1024];
            int bytesIn = 0;
            for (int i = 0; i < dirList.length; i++) {
                File f = new File(dirToZip, dirList[i]);
                if (f.isDirectory()) {
                    String parent = "";
                    if (path != null) parent = path + System.getProperty("file.separator");
                    parent += FileTool.getParentDirectory(f.getPath());
                    zipDirectory(f.getPath(), zos, parent);
                } else {
                    FileInputStream fis = new FileInputStream(f);
                    String parentPath = "";
                    if (path != null) parentPath = path + System.getProperty("file.separator");
                    ZipEntry anEntry = new ZipEntry(parentPath + FileTool.getFilenameWithExtension(f));
                    zos.putNextEntry(anEntry);
                    while ((bytesIn = fis.read(readBuffer)) != -1) {
                        zos.write(readBuffer, 0, bytesIn);
                    }
                    fis.close();
                }
            }
        } catch (Throwable e) {
            Logger.logException(e);
        }
    }
