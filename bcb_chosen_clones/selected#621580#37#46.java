    private static void addFile(final File file, final File dirToZip, final ZipOutputStream zos) throws IOException {
        final String absolutePath = file.getAbsolutePath();
        final int index = absolutePath.indexOf(dirToZip.getName());
        final String zipEntryName = absolutePath.substring(index, absolutePath.length());
        final byte[] b = new byte[(int) (file.length())];
        final ZipEntry cpZipEntry = new ZipEntry(zipEntryName);
        zos.putNextEntry(cpZipEntry);
        zos.write(b, 0, (int) file.length());
        zos.closeEntry();
    }
