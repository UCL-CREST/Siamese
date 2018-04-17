    private static void doCopyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
        if (destFile.exists() && destFile.isDirectory()) {
            throw new IOException("Destination '" + destFile + "' exists but is a directory");
        }
        FileChannel input = new FileInputStream(srcFile).getChannel();
        try {
            FileChannel output = new FileOutputStream(destFile).getChannel();
            try {
                output.transferFrom(input, 0, input.size());
            } finally {
                IOUtil.closeQuietly(output);
            }
        } finally {
            IOUtil.closeQuietly(input);
        }
        if (srcFile.length() != destFile.length()) {
            throw new IOException("Failed to copy full contents from '" + srcFile + "' to '" + destFile + "'");
        }
        if (preserveFileDate) {
            destFile.setLastModified(srcFile.lastModified());
        }
    }
