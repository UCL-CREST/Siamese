    public static void copy(final File src, final File dst) throws IOException, IllegalArgumentException {
        long fileSize = src.length();
        final FileInputStream fis = new FileInputStream(src);
        final FileOutputStream fos = new FileOutputStream(dst);
        final FileChannel in = fis.getChannel(), out = fos.getChannel();
        try {
            long offs = 0, doneCnt = 0;
            final long copyCnt = Math.min(65536, fileSize);
            do {
                doneCnt = in.transferTo(offs, copyCnt, out);
                offs += doneCnt;
                fileSize -= doneCnt;
            } while (fileSize > 0);
        } finally {
            try {
                in.close();
            } catch (final IOException e) {
            }
            try {
                out.close();
            } catch (final IOException e) {
            }
            try {
                fis.close();
            } catch (final IOException e) {
            }
            try {
                fos.close();
            } catch (final IOException e) {
            }
            src.delete();
        }
    }
