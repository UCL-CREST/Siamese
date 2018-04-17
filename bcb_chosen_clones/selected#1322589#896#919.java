    private void zipDir(String baseDir, String dir2zip, ZipOutputStream zos) throws IOException {
        File zipDir = new File(dir2zip);
        String[] dirList = zipDir.list();
        for (int i = 0; i < dirList.length; i++) {
            File f = new File(zipDir, dirList[i]);
            if (f.isDirectory()) {
                String filePath = f.getPath();
                zipDir(baseDir, filePath, zos);
            } else {
                FileInputStream fis = new FileInputStream(f);
                String basePath = new File(baseDir).getCanonicalPath();
                String entryName = f.getCanonicalPath();
                if (entryName.length() > basePath.length()) entryName = entryName.substring(basePath.length() + 1);
                ZipEntry anEntry = new ZipEntry(entryName);
                zos.putNextEntry(anEntry);
                byte[] readBuffer = new byte[512];
                int bytesIn = 0;
                while ((bytesIn = fis.read(readBuffer)) != -1) {
                    zos.write(readBuffer, 0, bytesIn);
                }
                fis.close();
            }
        }
    }
