    private static void createZip(final File destinationFile, final List<File> files, final int cutFileNameIndex) throws IOException {
        final ZipOutputStream oStream = new ZipOutputStream(new FileOutputStream(destinationFile));
        for (final File file : files) {
            final String fullFileName = file.getAbsolutePath();
            final String cuttedFileName = fullFileName.substring(cutFileNameIndex);
            final String osIndependendFileName = cuttedFileName.replace(File.separatorChar, '/');
            oStream.putNextEntry(new ZipEntry(osIndependendFileName));
            final FileInputStream iStream = new FileInputStream(file);
            FileOperations.copy(iStream, oStream);
            oStream.closeEntry();
            iStream.close();
        }
        oStream.close();
    }
