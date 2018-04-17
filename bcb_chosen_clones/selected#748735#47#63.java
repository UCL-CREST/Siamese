    public static OctetStream toZippedOctetStream(List<ZipEntry> entries, List<byte[]> files) throws java.io.IOException {
        if (entries.size() != files.size()) {
            return null;
        }
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(bytes);
        Iterator<ZipEntry> entriesItr = entries.iterator();
        Iterator<byte[]> filesItr = files.iterator();
        while (entriesItr.hasNext()) {
            byte[] file = filesItr.next();
            ZipEntry entry = entriesItr.next();
            zip.putNextEntry(entry);
            zip.write(file, 0, file.length);
        }
        zip.close();
        return new OctetStream(bytes.toByteArray());
    }
