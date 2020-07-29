    protected static void copy(InputStream in, String name, ZipOutputStream out) throws Exception {
        out.putNextEntry(new ZipEntry(name));
        int i;
        while ((i = in.read()) != -1) out.write(i);
    }
