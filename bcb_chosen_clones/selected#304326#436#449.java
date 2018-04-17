    public static void addFileToZip(File file, String destiniy, ZipOutputStream zos) {
        try {
            FileInputStream fis = new FileInputStream(file);
            ZipEntry entry = new ZipEntry(destiniy);
            zos.putNextEntry(entry);
            byte[] readBuffer = new byte[1024];
            int bytesIn = 0;
            while ((bytesIn = fis.read(readBuffer)) != -1) {
                zos.write(readBuffer, 0, bytesIn);
            }
            zos.closeEntry();
        } catch (Exception e) {
        }
    }
