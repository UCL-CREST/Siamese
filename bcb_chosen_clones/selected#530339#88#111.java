    public static void zipDir(File zipDir, ZipOutputStream zos) throws IOException {
        String[] dirList;
        if (zipDir.isDirectory()) {
            dirList = zipDir.list();
        } else {
            dirList = new String[] { zipDir.getAbsolutePath() };
        }
        byte[] readBuffer = new byte[2156];
        int bytesIn = 0;
        for (int i = 0; i < dirList.length; i++) {
            File f = new File(zipDir, dirList[i]);
            if (f.isDirectory()) {
                zipDir(f, zos);
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
    }
