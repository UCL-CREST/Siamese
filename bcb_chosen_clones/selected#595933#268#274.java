    public void store(String path, InputStream stream) throws IOException {
        toIgnore.add(normalizePath(path));
        ZipEntry entry = new ZipEntry(path);
        zipOutput.putNextEntry(entry);
        IOUtils.copy(stream, zipOutput);
        zipOutput.closeEntry();
    }
