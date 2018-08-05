    @Override
    public void copyFile2File(final File src, final File dest, final boolean force) throws C4JException {
        if (dest.exists()) if (force && !dest.delete()) throw new C4JException(format("Copying ‘%s’ to ‘%s’ failed; cannot overwrite existing file.", src.getPath(), dest.getPath()));
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            inChannel = new FileInputStream(src).getChannel();
            outChannel = new FileOutputStream(dest).getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
            if (src.canExecute()) dest.setExecutable(true, false);
        } catch (final IOException e) {
            throw new C4JException(format("Could not copy ‘%s’ to ‘%s’.", src.getPath(), dest.getPath()), e);
        } finally {
            if (inChannel != null) try {
                try {
                    inChannel.close();
                } catch (final IOException e) {
                    throw new C4JException(format("Could not close input stream for ‘%s’.", src.getPath()), e);
                }
            } finally {
                if (outChannel != null) try {
                    outChannel.close();
                } catch (final IOException e) {
                    throw new C4JException(format("Could not close output stream for ‘%s’.", dest.getPath()), e);
                }
            }
        }
    }
