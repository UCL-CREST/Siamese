    public static long checksum(IFile file) throws IOException {
        InputStream contents;
        try {
            contents = file.getContents();
        } catch (CoreException e) {
            throw new CausedIOException("Failed to calculate checksum.", e);
        }
        CheckedInputStream in = new CheckedInputStream(contents, new Adler32());
        try {
            IOUtils.copy(in, new NullOutputStream());
        } catch (IOException e) {
            throw new CausedIOException("Failed to calculate checksum.", e);
        } finally {
            IOUtils.closeQuietly(in);
        }
        return in.getChecksum().getValue();
    }
