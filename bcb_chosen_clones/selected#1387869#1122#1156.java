        public static int my_rename(String source, String dest) {
            logger.debug("RENAME " + source + " to " + dest);
            if (source == null || dest == null) return -1;
            {
                logger.debug("\tMoving file across file systems.");
                FileChannel srcChannel = null;
                FileChannel dstChannel = null;
                FileLock lock = null;
                try {
                    srcChannel = new FileInputStream(source).getChannel();
                    dstChannel = new FileOutputStream(dest).getChannel();
                    lock = dstChannel.lock();
                    dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
                    dstChannel.force(true);
                } catch (IOException e) {
                    logger.fatal("Error while copying file '" + source + "' to file '" + dest + "'. " + e.getMessage(), e);
                    return common_h.ERROR;
                } finally {
                    try {
                        lock.release();
                    } catch (Throwable t) {
                        logger.fatal("Error releasing file lock - " + dest);
                    }
                    try {
                        srcChannel.close();
                    } catch (Throwable t) {
                    }
                    try {
                        dstChannel.close();
                    } catch (Throwable t) {
                    }
                }
            }
            return common_h.OK;
        }
