    private static void compress(String eName, byte[] data, ZipOutputStream out, String basedir) throws IOException {
        ZipEntry entry = new ZipEntry(basedir + new String(eName.getBytes(), "utf-8"));
        out.putNextEntry(entry);
        IOUtils.write(data, out);
    }
