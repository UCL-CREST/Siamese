    private boolean saveOneZip(ZipOutputStream zout, String fname, byte buf[]) throws IOException {
        int sz;
        InputStream in;
        try {
            in = EUtil.U7openStream(fname);
            sz = in.available();
            if (buf == null || buf.length < sz) buf = new byte[sz];
            in.read(buf, 0, sz);
        } catch (IOException e) {
            return true;
        }
        ZipEntry entry = new ZipEntry(EUtil.baseName(fname));
        zout.putNextEntry(entry);
        zout.write(buf, 0, sz);
        in.close();
        return true;
    }
