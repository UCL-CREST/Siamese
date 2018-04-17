    public static void copyFile(String inName, String otName) throws Exception {
        File inFile = null;
        File otFile = null;
        try {
            inFile = new File(inName);
            otFile = new File(otName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (inFile == null || otFile == null) return;
        FileChannel sourceChannel = new FileInputStream(inFile).getChannel();
        FileChannel destinationChannel = new FileOutputStream(otFile).getChannel();
        sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
        sourceChannel.close();
        destinationChannel.close();
    }
