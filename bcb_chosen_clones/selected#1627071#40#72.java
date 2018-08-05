    public static String zipDirectory(String dirPath) throws Exception {
        File dir = new File(dirPath);
        String[] files = dir.list();
        if (files.length < 1) {
            return null;
        }
        File file = File.createTempFile(dir.getName(), "zip");
        FileOutputStream fos = new FileOutputStream(file);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        ZipOutputStream zos = new ZipOutputStream(bos);
        zos.setMethod(ZipOutputStream.DEFLATED);
        zos.setLevel(Deflater.BEST_COMPRESSION);
        byte[] data = new byte[BUFFER];
        for (int i = 0; i < files.length; i++) {
            FileInputStream fi = new FileInputStream(dirPath + File.separator + files[i]);
            BufferedInputStream buffer = new BufferedInputStream(fi, BUFFER);
            ZipEntry entry = new ZipEntry(normalizeName(files[i]));
            zos.putNextEntry(entry);
            int count;
            while ((count = buffer.read(data, 0, BUFFER)) != -1) {
                zos.write(data, 0, count);
            }
            zos.closeEntry();
            buffer.close();
        }
        zos.flush();
        bos.flush();
        fos.flush();
        zos.close();
        bos.close();
        fos.close();
        return file.getAbsolutePath();
    }
