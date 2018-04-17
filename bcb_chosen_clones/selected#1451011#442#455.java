    public static void zipTheFile(File theFileToZip) throws IOException {
        byte[] buf = new byte[8096];
        File zipFile = new File(theFileToZip.getParent(), theFileToZip.getName() + ".zip");
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
        FileInputStream fis = new FileInputStream(theFileToZip);
        zos.putNextEntry(new ZipEntry(theFileToZip.getName()));
        int lenght;
        while ((lenght = fis.read(buf)) > 0) {
            zos.write(buf, 0, lenght);
        }
        zos.closeEntry();
        fis.close();
        zos.close();
    }
