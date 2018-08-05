    public static byte[] zipToBuffer(InputStream is) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ZipOutputStream zips = new ZipOutputStream(bytes);
        zips.putNextEntry(new ZipEntry("main"));
        byte[] tmp = new byte[4096];
        int nread;
        while ((nread = is.read(tmp)) > 0) {
            zips.write(tmp, 0, nread);
        }
        zips.closeEntry();
        zips.close();
        return bytes.toByteArray();
    }
