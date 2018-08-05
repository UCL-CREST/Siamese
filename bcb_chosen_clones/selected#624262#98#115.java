    public static void zipFile(File source, File target) throws IOException {
        FileOutputStream fos = new FileOutputStream(target);
        ZipOutputStream zos = new ZipOutputStream(fos);
        FileInputStream fis = new FileInputStream(source);
        BufferedInputStream bis = new BufferedInputStream(fis);
        ZipEntry entry = new ZipEntry(source.getCanonicalFile().getName());
        zos.putNextEntry(entry);
        byte[] barray = new byte[1024];
        int bytes;
        while ((bytes = bis.read(barray, 0, 1024)) > -1) {
            zos.write(barray, 0, bytes);
        }
        bis.close();
        fis.close();
        zos.flush();
        zos.close();
        fos.close();
    }
