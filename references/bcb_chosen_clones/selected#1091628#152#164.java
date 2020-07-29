    private void write(final long begin, final long end, final File out) throws Exception {
        byte[] array = new byte[CLUSTER_SIZE];
        ByteBuffer buf;
        ZipOutputStream zipfile = new ZipOutputStream(new FileOutputStream(out));
        zipfile.putNextEntry(new ZipEntry("clusters_" + Long.toHexString(begin) + "_" + Long.toHexString(end) + ".dat"));
        for (long i = begin; i <= end; ++i) {
            buf = image.readClusterIntoBuffer(i, false);
            buf.get(array);
            zipfile.write(array);
        }
        zipfile.closeEntry();
        zipfile.close();
    }
