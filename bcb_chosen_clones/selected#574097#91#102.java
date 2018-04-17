    private void addFile(final File file) throws IOException {
        this.fileLength += file.length();
        this.fileCounter++;
        final String absolutePath = file.getAbsolutePath();
        final int index = absolutePath.indexOf(this.dirToStart);
        final String zipEntryName = absolutePath.substring(index, absolutePath.length());
        final byte[] b = new byte[(int) (file.length())];
        final ZipEntry cpZipEntry = new ZipEntry(zipEntryName);
        this.zos.putNextEntry(cpZipEntry);
        this.zos.write(b, 0, (int) file.length());
        this.zos.closeEntry();
    }
