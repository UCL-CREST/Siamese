    protected boolean copyFile(File sourceFile, File destinationFile) {
        try {
            FileChannel srcChannel = new FileInputStream(sourceFile).getChannel();
            FileChannel dstChannel = new FileOutputStream(destinationFile).getChannel();
            dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
            srcChannel.close();
            dstChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
