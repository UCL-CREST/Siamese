    protected void copy(File source, File destination) throws IOException {
        final FileChannel inChannel = new FileInputStream(source).getChannel();
        final FileChannel outChannel = new FileOutputStream(destination).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }
