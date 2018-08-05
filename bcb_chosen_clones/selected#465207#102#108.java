    public final void copyFile(final File fromFile, final File toFile) throws IOException {
        this.createParentPathIfNeeded(toFile);
        final FileChannel sourceChannel = new FileInputStream(fromFile).getChannel();
        final FileChannel targetChannel = new FileOutputStream(toFile).getChannel();
        final long sourceFileSize = sourceChannel.size();
        sourceChannel.transferTo(0, sourceFileSize, targetChannel);
    }
