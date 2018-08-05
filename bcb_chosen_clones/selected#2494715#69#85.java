    private static void doCopyFile(FileInputStream in, FileOutputStream out) {
        FileChannel inChannel = null, outChannel = null;
        try {
            inChannel = in.getChannel();
            outChannel = out.getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (IOException e) {
            throw ManagedIOException.manage(e);
        } finally {
            if (inChannel != null) {
                close(inChannel);
            }
            if (outChannel != null) {
                close(outChannel);
            }
        }
    }
