    @Override
    protected void writeRecord(Record record, int index) throws IOException {
        buffer.reset();
        RecordSelection.write(record, dataBuffer);
        final byte[] bytes = buffer.toByteArray();
        final ZipEntry entry = new ZipEntry(Integer.toString(index));
        entry.setSize(bytes.length);
        zip.putNextEntry(entry);
        zip.write(bytes);
        zip.closeEntry();
    }
