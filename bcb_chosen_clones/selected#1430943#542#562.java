    private void zipDir(ZipOutputStream zos, String rootDir, String dirToZip) throws IOException {
        File dirToZipAbs = new File(rootDir, dirToZip);
        byte[] readBuffer = new byte[2156];
        int bytesIn = 0;
        for (String file : dirToZipAbs.list()) {
            File fileToZip = new File(dirToZip, file);
            File fileToZipAbs = new File(dirToZipAbs, file);
            if (fileToZipAbs.isFile()) {
                ZipEntry ze = new ZipEntry(fileToZip.getPath());
                zos.putNextEntry(ze);
                FileInputStream fis = new FileInputStream(fileToZipAbs);
                while ((bytesIn = fis.read(readBuffer)) != -1) {
                    zos.write(readBuffer, 0, bytesIn);
                }
                fis.close();
                zos.closeEntry();
            } else {
                zipDir(zos, rootDir, fileToZip.getPath());
            }
        }
    }
