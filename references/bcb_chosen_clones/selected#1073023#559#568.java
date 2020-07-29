    public static final byte[] compress(String str) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ZipOutputStream zout = new ZipOutputStream(out);
        zout.putNextEntry(new ZipEntry("0"));
        zout.write(str.getBytes());
        zout.closeEntry();
        byte[] compressed = out.toByteArray();
        zout.close();
        return compressed;
    }
