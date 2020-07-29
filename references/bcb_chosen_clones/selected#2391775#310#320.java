    void write(OutputStream os) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(os);
        ListIterator iterator = entryList.listIterator();
        while (iterator.hasNext()) {
            Entry entry = (Entry) iterator.next();
            ZipEntry ze = entry.zipEntry;
            zos.putNextEntry(ze);
            zos.write(entry.bytes);
        }
        zos.close();
    }
