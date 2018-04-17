    private static void unpackEntry(File destinationFile, ZipInputStream zin, ZipEntry entry) throws Exception {
        if (!entry.isDirectory()) {
            createFolders(destinationFile.getParentFile());
            FileOutputStream fis = new FileOutputStream(destinationFile);
            try {
                IOUtils.copy(zin, fis);
            } finally {
                zin.closeEntry();
                fis.close();
            }
        } else {
            createFolders(destinationFile);
        }
    }
