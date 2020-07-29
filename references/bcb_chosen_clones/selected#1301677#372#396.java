    public static void zipDir(String dirPath, ZipOutputStream zos, int truncLen, String prefix) {
        try {
            File zipDir = new File(dirPath);
            String[] dirList = zipDir.list();
            byte[] readBuffer = new byte[1024];
            int bytesIn = 0;
            for (int i = 0; i < dirList.length; i++) {
                File f = new File(zipDir, dirList[i]);
                if (f.isDirectory()) {
                    String filePath = f.getPath();
                    zipDir(filePath, zos, truncLen, prefix);
                    continue;
                }
                FileInputStream fis = new FileInputStream(f);
                ZipEntry anEntry = new ZipEntry(prefix + f.getPath().substring(truncLen));
                zos.putNextEntry(anEntry);
                while ((bytesIn = fis.read(readBuffer)) != -1) {
                    zos.write(readBuffer, 0, bytesIn);
                }
                fis.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
