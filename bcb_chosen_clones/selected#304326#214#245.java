    public static void zipDir(String dirOrigen, String relPath, ZipOutputStream zos) {
        try {
            java.io.File zipDir = new java.io.File(dirOrigen);
            String[] dirList = zipDir.list();
            for (int i = 0; i < dirList.length; i++) {
                java.io.File f = new java.io.File(zipDir, dirList[i]);
                if (f.isDirectory()) {
                    String filePath = f.getAbsolutePath();
                    if (relPath != null && !relPath.equals("")) zipDir(filePath, relPath + "/" + f.getName(), zos); else zipDir(filePath, f.getName(), zos);
                    continue;
                }
                FileInputStream fis = new FileInputStream(f);
                String entryName = f.getName();
                if (relPath != null && !relPath.equals("")) {
                    entryName = relPath + "/" + entryName;
                }
                ZipEntry anEntry = new ZipEntry(entryName);
                try {
                    zos.putNextEntry(anEntry);
                    byte[] readBuffer = new byte[1024];
                    int bytesIn = 0;
                    while ((bytesIn = fis.read(readBuffer)) != -1) {
                        zos.write(readBuffer, 0, bytesIn);
                    }
                } catch (ZipException zipException) {
                }
                fis.close();
                zos.closeEntry();
            }
        } catch (Exception e) {
        }
    }
