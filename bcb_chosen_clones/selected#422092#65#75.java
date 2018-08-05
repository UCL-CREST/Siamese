    public static void compressZip(OutputStream out, String s, int level) throws IOException {
        final byte[] in = s.getBytes();
        final ZipOutputStream zout = new ZipOutputStream(out);
        zout.setLevel(level);
        zout.setComment("Data transfer");
        zout.putNextEntry(new ZipEntry("1"));
        zout.write(in, 0, in.length);
        zout.closeEntry();
        zout.flush();
        zout.finish();
    }
