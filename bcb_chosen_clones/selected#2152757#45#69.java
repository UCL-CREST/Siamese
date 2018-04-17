    public static void zipDirectoryContents(String dir, ZipOutputStream zos) {
        try {
            File dirToZip = new File(dir);
            String[] dirList = dirToZip.list();
            byte[] readBuffer = new byte[1024];
            int bytesIn = 0;
            for (int i = 0; i < dirList.length; i++) {
                File f = new File(dirToZip, dirList[i]);
                if (f.isDirectory()) {
                    zipDirectory(f.getPath(), zos, "symbols");
                } else {
                    FileInputStream fis = new FileInputStream(f);
                    String parentPath = "";
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
