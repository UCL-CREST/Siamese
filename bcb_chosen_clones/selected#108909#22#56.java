    public static boolean copy(File from, File to, Override override) throws IOException {
        FileInputStream in = null;
        FileOutputStream out = null;
        FileChannel srcChannel = null;
        FileChannel destChannel = null;
        if (override == null) override = Override.NEWER;
        switch(override) {
            case NEVER:
                if (to.isFile()) return false;
                break;
            case NEWER:
                if (to.isFile() && (from.lastModified() - LASTMODIFIED_DIFF_MILLIS) < to.lastModified()) return false;
                break;
        }
        to.getParentFile().mkdirs();
        try {
            in = new FileInputStream(from);
            out = new FileOutputStream(to);
            srcChannel = in.getChannel();
            destChannel = out.getChannel();
            long position = 0L;
            long count = srcChannel.size();
            while (position < count) {
                long chunk = Math.min(MAX_IO_CHUNK_SIZE, count - position);
                position += destChannel.transferFrom(srcChannel, position, chunk);
            }
            to.setLastModified(from.lastModified());
            return true;
        } finally {
            CommonUtils.close(srcChannel);
            CommonUtils.close(destChannel);
            CommonUtils.close(out);
            CommonUtils.close(in);
        }
    }
