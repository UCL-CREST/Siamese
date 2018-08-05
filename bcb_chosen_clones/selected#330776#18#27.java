    public static byte[] compress(String string) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ZipOutputStream zout = new ZipOutputStream(out);
        zout.putNextEntry(new ZipEntry("0"));
        zout.write(string.getBytes("UTF-8"));
        zout.closeEntry();
        byte[] compressed = out.toByteArray();
        zout.close();
        return compressed;
    }
