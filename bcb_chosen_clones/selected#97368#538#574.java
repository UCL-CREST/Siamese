    public static void createZipFile(final String zipFileName, final String pathDirectoryToZip, final String pathZipFile) throws Exception {
        if (zipFileName == null || zipFileName.length() == 0) {
            throw new IllegalArgumentException("zipFileName shouldn't be null");
        }
        if (pathDirectoryToZip == null || pathDirectoryToZip.length() == 0) {
            throw new IllegalArgumentException("pathDirectoryToZip shouldn't be null");
        }
        final File di = new File(pathDirectoryToZip);
        final File[] files1 = di.listFiles();
        final FileOutputStream dest = new FileOutputStream(pathZipFile + ZIP_FILE_SEP + zipFileName);
        final CheckedOutputStream checksum = new CheckedOutputStream(dest, new Adler32());
        final BufferedOutputStream buff = new BufferedOutputStream(checksum);
        final ZipOutputStream out = new ZipOutputStream(buff);
        out.setMethod(ZipOutputStream.DEFLATED);
        out.setLevel(Deflater.BEST_COMPRESSION);
        final byte[] data = new byte[NB_BITE];
        for (final File f : files1) {
            if (f.isDirectory()) {
                zipDir(f, "", out);
            } else {
                final FileInputStream fi = new FileInputStream(pathDirectoryToZip + ZIP_FILE_SEP + f.getName());
                final BufferedInputStream buffi = new BufferedInputStream(fi, NB_BITE);
                final ZipEntry entry = new ZipEntry(f.getName());
                out.putNextEntry(entry);
                int count;
                while ((count = buffi.read(data, 0, NB_BITE)) != -1) {
                    out.write(data, 0, count);
                }
                out.closeEntry();
                buffi.close();
            }
        }
        out.close();
        buff.close();
        checksum.close();
        dest.close();
    }
