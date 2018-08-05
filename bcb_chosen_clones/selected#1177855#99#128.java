    public void copy(final File source, final File target) throws FileSystemException {
        LogHelper.logMethod(log, toObjectString(), "copy(), source = " + source + ", target = " + target);
        FileChannel sourceChannel = null;
        FileChannel targetChannel = null;
        try {
            sourceChannel = new FileInputStream(source).getChannel();
            targetChannel = new FileOutputStream(target).getChannel();
            sourceChannel.transferTo(0L, sourceChannel.size(), targetChannel);
            log.info("Copied " + source + " to " + target);
        } catch (FileNotFoundException e) {
            throw new FileSystemException("Unexpected FileNotFoundException while copying a file", e);
        } catch (IOException e) {
            throw new FileSystemException("Unexpected IOException while copying a file", e);
        } finally {
            if (sourceChannel != null) {
                try {
                    sourceChannel.close();
                } catch (IOException e) {
                    log.error("IOException during source channel close after copy", e);
                }
            }
            if (targetChannel != null) {
                try {
                    targetChannel.close();
                } catch (IOException e) {
                    log.error("IOException during target channel close after copy", e);
                }
            }
        }
    }
