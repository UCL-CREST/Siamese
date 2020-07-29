    public byte[] toZip() throws IOException {
        ByteArrayOutputStream bstream = new ByteArrayOutputStream();
        ZipOutputStream zipstream = new ZipOutputStream(bstream);
        for (int i = 0; i < size(); i++) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            get(i).store(stream, "");
            zipstream.putNextEntry(new ZipEntry("" + i));
            zipstream.write(stream.toByteArray());
            zipstream.closeEntry();
        }
        return bstream.toByteArray();
    }
