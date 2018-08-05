    private static void copyFile(final File from, final File to) throws IOException {
        final FileChannel in = new FileInputStream(from).getChannel();
        final FileChannel out = new FileOutputStream(to).getChannel();
        in.transferTo(0, in.size(), out);
    }
