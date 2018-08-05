    protected static void addZipEntry(String name, String content, ZipOutputStream zout) throws IOException {
        ZipEntry ze = new ZipEntry(name);
        ze.setTime((new Date()).getTime());
        zout.putNextEntry(ze);
        zout.write(content.getBytes("iso-8859-1"));
        zout.closeEntry();
    }
