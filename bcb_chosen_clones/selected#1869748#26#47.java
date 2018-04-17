    public static FileOutputStream Compress(Object[] files) throws IOException {
        FileOutputStream dest = new FileOutputStream("fichier.zip");
        CheckedOutputStream checksum = new CheckedOutputStream(dest, new CRC32());
        BufferedOutputStream buff = new BufferedOutputStream(checksum);
        ZipOutputStream out = new ZipOutputStream(buff);
        out.setMethod(ZipOutputStream.DEFLATED);
        out.setLevel(9);
        for (int i = 0; i < files.length; i++) {
            FileInputStream fi = new FileInputStream((File) files[i]);
            BufferedInputStream buffi = new BufferedInputStream(fi, BUFFER);
            ZipEntry entry = new ZipEntry(((File) files[i]).getName());
            out.putNextEntry(entry);
            int count;
            while ((count = buffi.read(data, 0, BUFFER)) != -1) {
                out.write(data, 0, count);
            }
            out.closeEntry();
            buffi.close();
        }
        out.close();
        return dest;
    }
