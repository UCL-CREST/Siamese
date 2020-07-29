    public void compress() throws IOException {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        String temp = getRaw();
        ZipOutputStream out = new ZipOutputStream(byteArray);
        ZipEntry entry = new ZipEntry("test");
        byte[] b = temp.getBytes();
        out.putNextEntry(entry);
        out.write(b, 0, temp.length());
        out.finish();
        setCompressed(byteArray.toString());
    }
