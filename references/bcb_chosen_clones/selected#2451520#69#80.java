    public static void uncompress(File srcFile, File destFile) throws IOException {
        InputStream input = null;
        OutputStream output = null;
        try {
            input = new GZIPInputStream(new FileInputStream(srcFile));
            output = new BufferedOutputStream(new FileOutputStream(destFile));
            IOUtils.copyLarge(input, output);
        } finally {
            IOUtils.closeQuietly(output);
            IOUtils.closeQuietly(input);
        }
    }
