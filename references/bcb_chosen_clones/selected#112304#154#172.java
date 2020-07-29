    public void store(OutputStream out) throws IOException {
        int u;
        Iterator i;
        ZipEntry ze;
        Map.Entry entry;
        ZipOutputStream zip;
        u = 0;
        zip = new ZipOutputStream(out);
        synchronized (map_) {
            for (i = map_.entrySet().iterator(); i.hasNext(); ) {
                entry = (Map.Entry) i.next();
                ze = new ZipEntry((String) entry.getKey());
                zip.putNextEntry(ze);
                u++;
                zip.write((byte[]) entry.getValue());
                zip.closeEntry();
            }
        }
    }
