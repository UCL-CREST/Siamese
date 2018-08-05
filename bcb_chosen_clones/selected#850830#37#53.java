    protected void onlyFileCopy(File in, File out) throws IOException {
        FileChannel inChannel = new FileInputStream(in).getChannel();
        FileChannel outChannel = new FileOutputStream(out).getChannel();
        try {
            int maxCount = (1024 * 1024 * 64) - (1024 * 32);
            long size = inChannel.size();
            long pos = 0;
            while (pos < size) {
                pos += inChannel.transferTo(pos, maxCount, outChannel);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (inChannel != null) inChannel.close();
            if (outChannel != null) outChannel.close();
        }
    }
