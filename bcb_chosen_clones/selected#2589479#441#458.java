    private static void compress(ZipOutputStream zos, String rootpath, File source) throws IOException {
        if (source.isFile()) {
            ZipEntry zipEntry = new ZipEntry(source.getName());
            zos.putNextEntry(zipEntry);
            FileInputStream fis = new FileInputStream(source);
            byte[] buffer = new byte[1024];
            for (int length; (length = fis.read(buffer)) > 0; ) {
                zos.write(buffer, 0, length);
            }
            fis.close();
            zos.closeEntry();
        } else if (source.isDirectory()) {
            File[] files = source.listFiles();
            for (File file : files) {
                compress(zos, rootpath, file);
            }
        }
    }
