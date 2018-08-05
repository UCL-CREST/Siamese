    public static boolean copyFile(String sourceName, String destName) {
        FileChannel sourceChannel = null;
        FileChannel destChannel = null;
        boolean wasOk = false;
        try {
            sourceChannel = new FileInputStream(sourceName).getChannel();
            destChannel = new FileOutputStream(destName).getChannel();
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
            wasOk = true;
        } catch (Throwable exception) {
            logger.log(Level.SEVERE, "Exception in copyFile", exception);
        } finally {
            try {
                if (sourceChannel != null) sourceChannel.close();
            } catch (Throwable tt) {
            }
            try {
                if (destChannel != null) destChannel.close();
            } catch (Throwable tt) {
            }
        }
        return wasOk;
    }
