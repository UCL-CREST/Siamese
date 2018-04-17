    public void unpack(File destDirectory, boolean delete) {
        if (delete) delete(destDirectory);
        if (destDirectory.exists()) throw new ContentPackageException("Destination directory already exists.");
        this.destDirectory = destDirectory;
        this.manifestFile = new File(destDirectory, MANIFEST_FILE_NAME);
        try {
            if (zipInputStream == null) zipInputStream = new ZipInputStream(new FileInputStream(zipFile));
            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                File destFile = new File(destDirectory, zipEntry.getName());
                destFile.getParentFile().mkdirs();
                if (!zipEntry.isDirectory()) {
                    BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(destFile), BUFFER_SIZE);
                    byte[] buffer = new byte[BUFFER_SIZE];
                    int length;
                    while ((length = zipInputStream.read(buffer, 0, BUFFER_SIZE)) != -1) output.write(buffer, 0, length);
                    output.close();
                    zipInputStream.closeEntry();
                }
            }
            zipInputStream.close();
        } catch (IOException ex) {
            throw new ContentPackageException(ex);
        }
    }
