    public static void zipFile(File fileToZip, ZipOutputStream zos) throws IOException {
        if (fileToZip.isFile()) {
            String name = fileToZip.getName();
            zos.putNextEntry(new ZipEntry(name));
            FileInputStream fis = new FileInputStream(fileToZip);
            int c;
            while ((c = fis.read()) != -1) {
                zos.write(c);
            }
            fis.close();
            zos.closeEntry();
        }
    }
