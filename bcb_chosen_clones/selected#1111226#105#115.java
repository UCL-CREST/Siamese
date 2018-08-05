    @Override
    protected void writeExistings(HashSet<String> obsolete, ZipOutputStream zipOut) throws IOException {
        for (ItemData data : allData.values()) {
            if (!obsolete.contains(data.getName())) {
                ZipEntry newEntry = data.createZipEntry();
                zipOut.putNextEntry(newEntry);
                zipOut.write(data.getBytes());
                zipOut.closeEntry();
            }
        }
    }
