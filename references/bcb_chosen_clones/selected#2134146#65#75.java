    public void createEmptySource() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(buffer);
        ZipEntry defaultEntry = new ZipEntry("META-INF/created-by");
        defaultEntry.setTime(System.currentTimeMillis());
        zos.putNextEntry(defaultEntry);
        zos.write(getClass().getCanonicalName().getBytes());
        zos.close();
        byte data[] = buffer.toByteArray();
        source = new ByteArrayInputStream(data);
    }
