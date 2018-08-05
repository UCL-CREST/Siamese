    public static boolean packageZ(FilePackage fp, File destFile) {
        try {
            FileOutputStream fos = new FileOutputStream(destFile);
            ZipOutputStream zos = new ZipOutputStream(fos);
            zos.setMethod(ZipOutputStream.DEFLATED);
            zos.putNextEntry(new ZipEntry(DATA_STAMP));
            ObjectOutputStream oos = new ObjectOutputStream(zos);
            oos.writeObject(fp);
            zos.closeEntry();
            zos.flush();
            File[] sourcesFiles = fp.getFileList();
            for (int i = 0; i < sourcesFiles.length; i++) {
                File f = sourcesFiles[i];
                if (f.isDirectory()) {
                    debug(" is dir " + f);
                } else {
                    ZipEntry ze = new ZipEntry(fp.getName(f));
                    zos.putNextEntry(ze);
                    FileInputStream fis = new FileInputStream(f);
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    byte[] data = new byte[DATA_BLOCK_SIZE];
                    int bCnt;
                    while ((bCnt = bis.read(data, 0, DATA_BLOCK_SIZE)) != -1) {
                        zos.write(data, 0, bCnt);
                    }
                    zos.closeEntry();
                    zos.flush();
                }
            }
            zos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
