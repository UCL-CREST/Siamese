    private static final void cloneFile(File origin, File target) throws IOException {
        FileChannel srcChannel = null;
        FileChannel destChannel = null;
        try {
            srcChannel = new FileInputStream(origin).getChannel();
            destChannel = new FileOutputStream(target).getChannel();
            destChannel.transferFrom(srcChannel, 0, srcChannel.size());
        } finally {
            if (srcChannel != null) srcChannel.close();
            if (destChannel != null) destChannel.close();
        }
    }
