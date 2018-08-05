    public void saveChanges() throws IOException {
        zipFile.delete();
        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile));
        for (String entryName : entryMap.keySet()) {
            zipOutputStream.putNextEntry(new ZipEntry(entryName));
            zipOutputStream.write(entryMap.get(entryName));
            zipOutputStream.closeEntry();
        }
        zipOutputStream.flush();
        try {
            zipOutputStream.close();
        } catch (ZipException e) {
        }
    }
