    private static void copyFile(File srcFile, File destFile, long chunkSize) throws IOException {
        FileInputStream is = null;
        FileOutputStream os = null;
        try {
            is = new FileInputStream(srcFile);
            FileChannel iChannel = is.getChannel();
            os = new FileOutputStream(destFile, false);
            FileChannel oChannel = os.getChannel();
            long doneBytes = 0L;
            long todoBytes = srcFile.length();
            while (todoBytes != 0L) {
                long iterationBytes = Math.min(todoBytes, chunkSize);
                long transferredLength = oChannel.transferFrom(iChannel, doneBytes, iterationBytes);
                if (iterationBytes != transferredLength) {
                    throw new IOException("Error during file transfer: expected " + iterationBytes + " bytes, only " + transferredLength + " bytes copied.");
                }
                doneBytes += transferredLength;
                todoBytes -= transferredLength;
            }
        } finally {
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
        }
        boolean successTimestampOp = destFile.setLastModified(srcFile.lastModified());
        if (!successTimestampOp) {
            log.warn("Could not change timestamp for {}. Index synchronization may be slow.", destFile);
        }
    }
