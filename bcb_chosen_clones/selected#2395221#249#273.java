    public static boolean copyFile(File source, File dest) {
        FileChannel srcChannel = null;
        FileChannel dstChannel = null;
        try {
            srcChannel = new FileInputStream(source).getChannel();
            dstChannel = new FileOutputStream(dest).getChannel();
            dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (srcChannel != null) {
                    srcChannel.close();
                }
            } catch (IOException e) {
            }
            try {
                if (dstChannel != null) {
                    dstChannel.close();
                }
            } catch (IOException e) {
            }
        }
        return true;
    }
