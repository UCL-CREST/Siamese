    void initOut() throws IOException {
        if (zipped) {
            zout = new ZipOutputStream(sink);
            zout.putNextEntry(new ZipEntry("file.xml"));
            this.xout = new OutputStreamWriter(zout, "UTF8");
        } else {
            this.xout = new OutputStreamWriter(sink, "UTF8");
        }
    }
