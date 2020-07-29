    public static void deleteTree(final File directory) throws IOException {
        if (false == directory.exists()) {
            fail("Cannot delete tree.  Does not exist:" + directory);
        }
        if (false == directory.isDirectory()) {
            fail("Cannot delete tree.  Not a directory:" + directory);
        }
        if (false == directory.canRead()) {
            if (false == directory.setReadable(true)) {
                fail("Cannot delete tree.  Cannot read:" + directory);
            }
        }
        if (false == directory.canWrite()) {
            if (false == directory.setWritable(true)) {
                fail("Cannot delete tree.  Cannot write:" + directory);
            }
        }
        final File[] files = directory.listFiles();
        for (final File file : files) {
            if (file.isFile()) {
                if (false == file.delete()) {
                    throw new IOException("Cannot delete:" + file);
                }
            }
            if (file.isDirectory()) {
                deleteTree(file);
            }
        }
        if (false == directory.delete()) {
            throw new IOException("Cannot delete:" + directory);
        }
    }
