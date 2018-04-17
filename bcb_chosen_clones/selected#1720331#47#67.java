    private static void zipDir(String basedir, String dir2zip, ZipOutputStream zos) throws IOException {
        File zipDir = new File(basedir + dirSep + dir2zip);
        String[] dirList = zipDir.list();
        byte[] readBuffer = new byte[2156];
        int bytesIn = 0;
        for (int i = 0; i < dirList.length; i++) {
            File f = new File(zipDir, dirList[i]);
            if (f.isDirectory()) {
                String filePath = dir2zip + dirSep + f.getName();
                zipDir(basedir, filePath, zos);
                continue;
            }
            FileInputStream fis = new FileInputStream(f);
            ZipEntry anEntry = new ZipEntry(dir2zip + dirSep + f.getName());
            zos.putNextEntry(anEntry);
            while ((bytesIn = fis.read(readBuffer)) != -1) {
                zos.write(readBuffer, 0, bytesIn);
            }
            fis.close();
        }
    }
