    public static void copyFile(File source, File dest) throws Exception {
        log.warn("File names are " + source.toString() + "   and " + dest.toString());
        if (!dest.getParentFile().exists()) dest.getParentFile().mkdir();
        FileChannel sourceChannel = new FileInputStream(source).getChannel();
        FileChannel destinationChannel = new FileOutputStream(dest).getChannel();
        sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
        sourceChannel.close();
        destinationChannel.close();
    }
