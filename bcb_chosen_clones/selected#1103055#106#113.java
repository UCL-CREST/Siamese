    public void writeToZip(byte b[], int off, int len) throws IOException {
        if (zipstream == null) {
            response.setContentType("application/vnd.google-earth.kmzl; charset=UTF-8");
            zipstream = new ZipOutputStream(output);
            zipstream.putNextEntry(new ZipEntry(kmlFileName));
        }
        zipstream.write(b, off, len);
    }
