    private void copyStream(InputStream in, ArchiveOutputStream out, ArchiveEntry entry) throws IOException {
        out.putArchiveEntry(entry);
        IOUtils.copy(in, out);
        out.closeArchiveEntry();
    }
