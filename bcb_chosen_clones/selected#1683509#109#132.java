    private static void zipFiles(ZipOutputStream zipout, File file, File sourceDirectory) throws IOException {
        if (file.isDirectory()) {
            for (File subFile : file.listFiles()) {
                zipFiles(zipout, subFile, sourceDirectory);
            }
        } else {
            final String entryName = getZipEntryName(file, sourceDirectory);
            LOG.debug("Zipping file '" + file.getAbsolutePath() + "' as entry '" + entryName + "'.");
            final ZipEntry entry = new ZipEntry(entryName);
            BufferedInputStream fileInput = null;
            try {
                fileInput = new BufferedInputStream(new FileInputStream(file), BUFFER_SIZE);
                byte[] buffer = new byte[BUFFER_SIZE];
                zipout.putNextEntry(entry);
                int count;
                while ((count = fileInput.read(buffer, 0, BUFFER_SIZE)) != -1) {
                    zipout.write(buffer, 0, count);
                }
                zipout.closeEntry();
            } finally {
                FileUtil.closeCloseable(fileInput);
            }
        }
    }
