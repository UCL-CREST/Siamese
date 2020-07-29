    public void zipDir(FileObject dir2zip, ZipOutputStream zos, String fileName) {
        try {
            FileObject[] dirList = dir2zip.getChildren();
            byte[] readBuffer = new byte[2156];
            int bytesIn = 0;
            for (int i = 0; i < dirList.length; i++) {
                FileObject f = dirList[i];
                if (f.isFolder()) {
                    zipDir(f, zos, fileName);
                    continue;
                }
                InputStream fis = f.getInputStream();
                if (!f.getNameExt().equals(fileName) && !f.getNameExt().startsWith(".")) {
                    String filePathName = f.getPath().replaceAll(context.getProjectDirectory().getPath(), "");
                    ZipEntry anEntry = new ZipEntry(filePathName);
                    zos.putNextEntry(anEntry);
                    while ((bytesIn = fis.read(readBuffer)) != -1) {
                        zos.write(readBuffer, 0, bytesIn);
                    }
                    fis.close();
                }
            }
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "Error creating ZIP file!");
        }
    }
