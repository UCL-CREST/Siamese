    private void copyFileTo(File destination) throws IOException {
        logger.fine("Copying from " + destination + "...");
        FileChannel srcChannel = new FileInputStream(getAbsolutePath()).getChannel();
        logger.fine("...got source channel " + srcChannel + "...");
        FileChannel destChannel = new FileOutputStream(new File(destination.getAbsolutePath())).getChannel();
        logger.fine("...got destination channel " + destChannel + "...");
        logger.fine("...Got channels...");
        destChannel.transferFrom(srcChannel, 0, srcChannel.size());
        logger.fine("...transferred.");
        srcChannel.close();
        destChannel.close();
    }
