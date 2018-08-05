    public static void unzip(File sourceZipFile, File unzipDestinationDirectory, FileFilter filter) throws IOException {
        unzipDestinationDirectory.mkdirs();
        if (!unzipDestinationDirectory.exists()) {
            throw new IOException("Unable to create destination directory: " + unzipDestinationDirectory);
        }
        ZipFile zipFile;
        zipFile = new ZipFile(sourceZipFile, ZipFile.OPEN_READ);
        Enumeration<? extends ZipEntry> zipFileEntries = zipFile.entries();
        while (zipFileEntries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
            if (!entry.isDirectory()) {
                String currentEntry = entry.getName();
                File destFile = new File(unzipDestinationDirectory, currentEntry);
                if (filter == null || filter.accept(destFile)) {
                    File destinationParent = destFile.getParentFile();
                    destinationParent.mkdirs();
                    BufferedInputStream is = new BufferedInputStream(zipFile.getInputStream(entry));
                    FileOutputStream fos = new FileOutputStream(destFile);
                    IOUtils.copyLarge(is, fos);
                    fos.flush();
                    IOUtils.closeQuietly(fos);
                }
            }
        }
        zipFile.close();
    }
