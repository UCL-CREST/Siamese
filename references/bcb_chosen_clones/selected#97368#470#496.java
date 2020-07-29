    private static void zipDir(final File zipDir, final String parentRelativePath, final ZipOutputStream zos) throws IOException {
        if (zipDir == null || zos == null) {
            throw new IllegalArgumentException("directory and ZipOutputStream shouldn't be null");
        }
        final File[] dirChildren = zipDir.listFiles();
        String entry = zipDir.getName();
        if (parentRelativePath.length() > 0) {
            entry = parentRelativePath + ZIP_FILE_SEP + zipDir.getName();
        }
        final ZipEntry anDirectoryEntry = new ZipEntry(entry + ZIP_FILE_SEP);
        zos.putNextEntry(anDirectoryEntry);
        final byte[] readBuffer = new byte[NB_BITE];
        int bytesIn = 0;
        for (final File element : dirChildren) {
            if (element.isDirectory()) {
                zipDir(element, entry, zos);
            } else {
                final FileInputStream fis = new FileInputStream(element);
                final ZipEntry anFileEntry = new ZipEntry(entry + ZIP_FILE_SEP + element.getName());
                zos.putNextEntry(anFileEntry);
                while ((bytesIn = fis.read(readBuffer)) != -1) {
                    zos.write(readBuffer, 0, bytesIn);
                }
                fis.close();
            }
        }
    }
