    public static void zipDir(String dir2zip, ZipOutputStream zos, String inFolder) throws IOException {
        File zipDir = new File(dir2zip);
        String[] dirList = zipDir.list();
        byte[] readBuffer = new byte[BUFFER];
        int bytesIn = 0;
        for (String dir : dirList) {
            File f = new File(zipDir, dir);
            if (f.isDirectory()) {
                String filePath = f.getPath();
                zipDir(filePath, zos, inFolder);
                continue;
            }
            FileInputStream fis = new FileInputStream(f);
            String fileZipPath = f.getPath().replace(inFolder, "");
            ZipEntry anEntry = new ZipEntry(fileZipPath);
            zos.putNextEntry(anEntry);
            while ((bytesIn = fis.read(readBuffer)) != -1) {
                zos.write(readBuffer, 0, bytesIn);
            }
            fis.close();
        }
    }
