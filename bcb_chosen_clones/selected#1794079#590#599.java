    public static byte[] zip(byte[] data) throws IOException {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        ZipOutputStream out = new ZipOutputStream(buf);
        ZipEntry entry = new ZipEntry("data");
        out.putNextEntry(entry);
        out.write(data);
        out.flush();
        out.close();
        return buf.toByteArray();
    }
