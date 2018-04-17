    public static void copyFile(File source, File destination) throws IOException {
        FileInputStream fis = new FileInputStream(source);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(destination);
            FileChannel sourceChannel = fis.getChannel();
            FileChannel destinationChannel = fos.getChannel();
            sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
            destinationChannel.close();
            sourceChannel.close();
        } finally {
            if (fos != null) fos.close();
            fis.close();
        }
    }
