    public LocalCompilation writeSuccessor(HashSet<String> obsolete, Collection<ItemData> newItems, int version) throws IOException {
        File path = getPath();
        path.mkdirs();
        File tempFile = new File(path, TEMP_FILE);
        ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(tempFile), BUFFER));
        try {
            if (obsolete != null) {
                writeExistings(obsolete, zipOut);
            }
            for (ItemData item : newItems) {
                if (item.isExisting()) {
                    zipOut.putNextEntry(item.createZipEntry());
                    zipOut.write(item.getBytes());
                    zipOut.closeEntry();
                }
            }
        } finally {
            zipOut.close();
        }
        File next = new File(getPath(), PREFIX + version);
        if (next.exists()) {
            next.delete();
        }
        tempFile.renameTo(next);
        return new LocalCompilation(next, getCompilationType());
    }
