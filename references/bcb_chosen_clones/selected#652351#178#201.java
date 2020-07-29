    public static void zipDir(String dir2zip, ZipOutputStream zos) {
        try {
            File zipDir = new File(dir2zip);
            String[] dirList = zipDir.list();
            byte[] readBuffer = new byte[2156];
            int bytesIn = 0;
            for (int i = 0; i < dirList.length; i++) {
                File f = new File(zipDir, dirList[i]);
                if (f.isDirectory()) {
                    String filePath = f.getPath();
                    zipDir(filePath, zos);
                    continue;
                }
                FileInputStream fis = new FileInputStream(f);
                ZipEntry anEntry = new ZipEntry(f.getPath());
                zos.putNextEntry(anEntry);
                while ((bytesIn = fis.read(readBuffer)) != -1) {
                    zos.write(readBuffer, 0, bytesIn);
                }
                fis.close();
            }
        } catch (Exception e) {
        }
    }
