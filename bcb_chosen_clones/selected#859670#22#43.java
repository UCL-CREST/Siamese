    private static void zipDirectory(String dir2zip, ZipOutputStream zos, String zipPath) throws IOException, IllegalArgumentException {
        File zipDir = new File(dir2zip);
        String[] dirList = zipDir.list();
        byte[] readBuffer = new byte[2156];
        int bytesIn = 0;
        for (int i = 0; i < dirList.length; i++) {
            File f = new File(zipDir, dirList[i]);
            if (f.isDirectory()) {
                String filePath = f.getPath();
                zipDirectory(filePath, zos, zipPath);
                continue;
            }
            FileInputStream fis = new FileInputStream(f);
            String path = f.getPath().substring(zipPath.length());
            ZipEntry anEntry = new ZipEntry(path);
            zos.putNextEntry(anEntry);
            while ((bytesIn = fis.read(readBuffer)) != -1) {
                zos.write(readBuffer, 0, bytesIn);
            }
            fis.close();
        }
    }
