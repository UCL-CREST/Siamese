    public static boolean copyFile(final File src, final File dst) throws FileNotFoundException {
        if (src == null || dst == null || src.equals(dst)) {
            return false;
        }
        boolean result = false;
        if (src.exists()) {
            if (dst.exists() && !dst.canWrite()) {
                return false;
            }
            final FileInputStream srcStream = new FileInputStream(src);
            final FileOutputStream dstStream = new FileOutputStream(dst);
            final FileChannel srcChannel = srcStream.getChannel();
            final FileChannel dstChannel = dstStream.getChannel();
            FileLock dstLock = null;
            FileLock srcLock = null;
            try {
                srcLock = srcChannel.tryLock(0, Long.MAX_VALUE, true);
                dstLock = dstChannel.tryLock();
                if (srcLock != null && dstLock != null) {
                    int maxCount = 64 * 1024 * 1024 - 32 * 1024;
                    long size = srcChannel.size();
                    long position = 0;
                    while (position < size) {
                        position += srcChannel.transferTo(position, maxCount, dstChannel);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (srcChannel != null) {
                    try {
                        if (srcLock != null) {
                            srcLock.release();
                        }
                        srcChannel.close();
                        srcStream.close();
                    } catch (IOException ex) {
                        Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if (dstChannel != null) {
                    try {
                        if (dstLock != null) {
                            dstLock.release();
                        }
                        dstChannel.close();
                        dstStream.close();
                        result = true;
                    } catch (IOException ex) {
                        Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return result;
    }
