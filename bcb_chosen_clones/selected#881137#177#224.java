    private void chopFileDisk() throws IOException {
        File tempFile = new File("" + logFile + ".tmp");
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        long startCopyPos;
        byte readBuffer[] = new byte[2048];
        int readCount;
        long totalBytesRead = 0;
        if (reductionRatio > 0 && logFile.length() > 0) {
            startCopyPos = logFile.length() / reductionRatio;
        } else {
            startCopyPos = 0;
        }
        try {
            bis = new BufferedInputStream(new FileInputStream(logFile));
            bos = new BufferedOutputStream(new FileOutputStream(tempFile));
            do {
                readCount = bis.read(readBuffer, 0, readBuffer.length);
                if (readCount > 0) {
                    totalBytesRead += readCount;
                    if (totalBytesRead > startCopyPos) {
                        bos.write(readBuffer, 0, readCount);
                    }
                }
            } while (readCount > 0);
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException ex) {
                }
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException ex) {
                }
            }
        }
        if (tempFile.isFile()) {
            if (!logFile.delete()) {
                throw new IOException("Error when attempting to delete the " + logFile + " file.");
            }
            if (!tempFile.renameTo(logFile)) {
                throw new IOException("Error when renaming the " + tempFile + " to " + logFile + ".");
            }
        }
    }
