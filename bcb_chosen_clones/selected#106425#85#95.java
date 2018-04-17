    public void fileCopy2(File inFile, File outFile) throws Exception {
        try {
            FileChannel srcChannel = new FileInputStream(inFile).getChannel();
            FileChannel dstChannel = new FileOutputStream(outFile).getChannel();
            dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
            srcChannel.close();
            dstChannel.close();
        } catch (IOException e) {
            throw new Exception("Could not copy file: " + inFile.getName());
        }
    }
