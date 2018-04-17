    public static byte[] zip(String contentlabel, byte[] inbuf) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ZipOutputStream zipout = new ZipOutputStream(bout);
        ZipEntry ze = new ZipEntry(contentlabel);
        zipout.putNextEntry(ze);
        zipout.setLevel(7);
        zipout.write(inbuf);
        zipout.closeEntry();
        zipout.close();
        return bout.toByteArray();
    }
