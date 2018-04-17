    @Override
    public int write(FileStatus.FileTrackingStatus fileStatus, InputStream input, PostWriteAction postWriteAction) throws WriterException, InterruptedException {
        String key = logFileNameExtractor.getFileName(fileStatus);
        int wasWritten = 0;
        FileOutputStreamPool fileOutputStreamPool = fileOutputStreamPoolFactory.getPoolForKey(key);
        RollBackOutputStream outputStream = null;
        File file = null;
        try {
            file = getOutputFile(key);
            lastWrittenFile = file;
            outputStream = fileOutputStreamPool.open(key, compressionCodec, file, true);
            outputStream.mark();
            wasWritten = IOUtils.copy(input, outputStream);
            if (postWriteAction != null) {
                postWriteAction.run(wasWritten);
            }
        } catch (Throwable t) {
            LOG.error(t.toString(), t);
            if (outputStream != null && wasWritten > 0) {
                LOG.error("Rolling back file " + file.getAbsolutePath());
                try {
                    outputStream.rollback();
                } catch (IOException e) {
                    throwException(e);
                } catch (InterruptedException e) {
                    throw e;
                }
            }
            throwException(t);
        } finally {
            try {
                fileOutputStreamPool.releaseFile(key);
            } catch (IOException e) {
                throwException(e);
            }
        }
        return wasWritten;
    }
