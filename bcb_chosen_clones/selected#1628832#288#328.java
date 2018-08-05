    public boolean moveFileSafely(final File in, final File out) throws IOException {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        final File tempOut = File.createTempFile("move", ".tmp");
        try {
            fis = new FileInputStream(in);
            fos = new FileOutputStream(tempOut);
            inChannel = fis.getChannel();
            outChannel = fos.getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            try {
                if (inChannel != null) inChannel.close();
            } catch (IOException e) {
                LogUtils.debugf(JRobinConverter.class, "failed to close channel %s", inChannel);
            }
            try {
                if (outChannel != null) outChannel.close();
            } catch (IOException e) {
                LogUtils.debugf(JRobinConverter.class, "failed to close channel %s", outChannel);
            }
            try {
                if (fis != null) fis.close();
            } catch (IOException e) {
                LogUtils.debugf(JRobinConverter.class, "failed to close stream %s", fis);
            }
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
                LogUtils.debugf(JRobinConverter.class, "failed to close stream %s", fos);
            }
        }
        out.delete();
        if (!out.exists()) {
            tempOut.renameTo(out);
            return in.delete();
        }
        return false;
    }
