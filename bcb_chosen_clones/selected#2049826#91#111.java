    protected static void zipDir(File zipDir, ZipOutputStream zos) throws IOException {
        String[] dirList = zipDir.list();
        byte[] readBuffer = new byte[2156];
        for (String file : dirList) {
            File f = new File(zipDir, file);
            if (f.isDirectory()) {
                ZipEntry anEntry = new ZipEntry(f.getName() + '/');
                zos.putNextEntry(anEntry);
                zipDir(f, zos);
            } else {
                ZipEntry anEntry = new ZipEntry(f.getName());
                zos.putNextEntry(anEntry);
                FileInputStream fis = new FileInputStream(f);
                int bytesIn;
                while ((bytesIn = fis.read(readBuffer)) != -1) {
                    zos.write(readBuffer, 0, bytesIn);
                }
                fis.close();
            }
        }
    }
