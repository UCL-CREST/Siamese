    public static void zipDir(String relativeTo, String dir2zip, ZipOutputStream zos) throws Exception {
        File zipDir = new File(dir2zip);
        String[] dirList = zipDir.list();
        byte[] readBuffer = new byte[2156];
        int bytesIn = 0;
        for (int i = 0; i < dirList.length; i++) {
            File f = new File(zipDir, dirList[i]);
            if (f.isDirectory()) {
                if (f.getName().equals("CVS")) {
                    continue;
                }
                String filePath = f.getPath();
                zipDir(relativeTo, filePath, zos);
                continue;
            }
            if (f.getName().startsWith("save-")) {
                continue;
            }
            FileInputStream fis = new FileInputStream(f);
            ZipEntry anEntry = new ZipEntry(f.getPath().substring(relativeTo.length()));
            zos.putNextEntry(anEntry);
            while ((bytesIn = fis.read(readBuffer)) != -1) {
                zos.write(readBuffer, 0, bytesIn);
            }
            zos.closeEntry();
            fis.close();
        }
    }
