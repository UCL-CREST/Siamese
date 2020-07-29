    private static void zipDir(String dir2zip, ZipOutputStream zos, String originalFolder) throws FileNotFoundException, IOException, ZipException {
        File zipDir = new File(dir2zip);
        String[] dirList = zipDir.list();
        byte[] readBuffer = new byte[2156];
        int bytesIn = 0;
        for (int i = 0; i < dirList.length; i++) {
            File f = new File(zipDir, dirList[i]);
            if (f.isDirectory()) {
                String filePath = f.getPath();
                zipDir(filePath, zos, originalFolder);
                continue;
            }
            FileInputStream fis = new FileInputStream(f);
            String path = f.getPath();
            path = path.replace(originalFolder + File.separator, "");
            while (path.contains("\\")) {
                path = path.replace("\\", "/");
            }
            ZipEntry anEntry = new ZipEntry(path);
            anEntry.setTime(f.lastModified());
            zos.putNextEntry(anEntry);
            while ((bytesIn = fis.read(readBuffer)) != -1) {
                zos.write(readBuffer, 0, bytesIn);
            }
            zos.closeEntry();
            anEntry.setTime(f.lastModified());
            fis.close();
        }
    }
