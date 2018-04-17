    public static void copy(final File src, File dst, final boolean overwrite) throws IOException, IllegalArgumentException {
        if (!src.isFile() || !src.exists()) {
            throw new IllegalArgumentException("Source file '" + src.getAbsolutePath() + "' not found!");
        }
        if (dst.exists()) {
            if (dst.isDirectory()) {
                dst = new File(dst, src.getName());
            } else if (dst.isFile()) {
                if (!overwrite) {
                    throw new IllegalArgumentException("Destination file '" + dst.getAbsolutePath() + "' already exists!");
                }
            } else {
                throw new IllegalArgumentException("Invalid destination object '" + dst.getAbsolutePath() + "'!");
            }
        }
        final File dstParent = dst.getParentFile();
        if (!dstParent.exists()) {
            if (!dstParent.mkdirs()) {
                throw new IOException("Failed to create directory " + dstParent.getAbsolutePath());
            }
        }
        long fileSize = src.length();
        if (fileSize > 20971520l) {
            final FileInputStream in = new FileInputStream(src);
            final FileOutputStream out = new FileOutputStream(dst);
            try {
                int doneCnt = -1;
                final int bufSize = 32768;
                final byte buf[] = new byte[bufSize];
                while ((doneCnt = in.read(buf, 0, bufSize)) >= 0) {
                    if (doneCnt == 0) {
                        Thread.yield();
                    } else {
                        out.write(buf, 0, doneCnt);
                    }
                }
                out.flush();
            } finally {
                try {
                    in.close();
                } catch (final IOException e) {
                }
                try {
                    out.close();
                } catch (final IOException e) {
                }
            }
        } else {
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
            }
        }
    }
