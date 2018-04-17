    private void zipFile(File f, ZipOutputStream zos) throws Exception {
        byte[] readBuffer = new byte[2048];
        int bytesIn = 0;
        FileInputStream fis = new FileInputStream(f);
        ZipEntry anEntry = new ZipEntry(getPath(f.getPath(), _startDir2zip));
        zos.putNextEntry(anEntry);
        while ((bytesIn = fis.read(readBuffer)) != -1) {
            zos.write(readBuffer, 0, bytesIn);
        }
        fis.close();
    }
