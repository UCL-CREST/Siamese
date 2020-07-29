    protected void addEntryToZip(JarOutputStream out, ZipEntry entry, InputStream in) throws IOException {
        out.putNextEntry(entry);
        IOUtils.copy(in, out);
        out.closeEntry();
    }
