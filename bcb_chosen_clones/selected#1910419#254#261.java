    private static void addEntry(ZipOutputStream zout, String name, String content) throws IOException {
        ZipEntry entry = new ZipEntry(name);
        zout.putNextEntry(entry);
        PrintWriter out = new PrintWriter(zout);
        out.write(content);
        out.flush();
        zout.closeEntry();
    }
