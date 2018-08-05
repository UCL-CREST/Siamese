    public static int zip(Resource resource, List names, OutputStream out) throws IOException {
        ZipOutputStream zip;
        InputStream in;
        Iterator i;
        String s;
        byte[] buf;
        int n;
        int u;
        if (out instanceof ZipOutputStream) {
            zip = (ZipOutputStream) out;
        } else {
            zip = new ZipOutputStream(out);
        }
        buf = new byte[BUFFER_SIZE];
        for (u = 0, i = names.iterator(); i.hasNext(); ) {
            s = (String) i.next();
            in = resource.getInputStream(s);
            if (in == null) {
                continue;
            }
            zip.putNextEntry(new ZipEntry(s));
            u++;
            while ((n = in.read(buf)) > 0) {
                zip.write(buf, 0, n);
            }
            in.close();
            zip.closeEntry();
        }
        if (u > 0) {
            zip.close();
        }
        return u;
    }
