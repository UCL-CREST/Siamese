    protected long copyFile(File fromFile, File toFile) throws BackupException {
        FileOutputStream fos = null;
        FileInputStream fis = null;
        CRC32 crc = new CRC32();
        if (logger.isDebugEnabled()) {
            logger.debug("Copying file: from=" + fromFile.getAbsolutePath() + " to=" + toFile.getAbsolutePath());
        }
        try {
            fis = new FileInputStream(fromFile);
        } catch (IOException e) {
            throw new BackupException(formatErrorMessage("Unable to open input file for writing", null, fromFile), e);
        }
        try {
            fos = new FileOutputStream(toFile);
        } catch (IOException e) {
            throw new BackupException(formatErrorMessage("Unable to open output file for writing", null, toFile), e);
        }
        long written = 0;
        try {
            byte[] data = new byte[BUFFER_SIZE];
            int len = -1;
            while ((len = fis.read(data)) > -1) {
                fos.write(data, 0, len);
                crc.update(data, 0, len);
                written += len;
            }
        } catch (IOException e) {
            throw new BackupException(formatErrorMessage("File copy operation failed", null, fromFile), e);
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
            }
            try {
                fos.close();
            } catch (IOException e) {
            }
        }
        if (written != fromFile.length()) {
            throw new BackupException("Written file length does not match size of input file: input file=" + fromFile.getAbsolutePath() + " input length=" + fromFile.length() + " written length=" + written);
        }
        return crc.getValue();
    }
