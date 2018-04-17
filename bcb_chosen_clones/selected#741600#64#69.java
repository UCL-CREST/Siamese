    void serialize(ZipOutputStream out) throws IOException {
        if ("imsmanifest.xml".equals(getFullName())) return;
        out.putNextEntry(new ZipEntry(getFullName()));
        IOUtils.copy(getDataStream(), out);
        out.closeEntry();
    }
