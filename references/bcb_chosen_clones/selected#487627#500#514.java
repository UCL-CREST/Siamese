    private static void copyFile(File source, File destination) throws IOException, SecurityException {
        if (!destination.exists()) destination.createNewFile();
        FileChannel sourceChannel = null;
        FileChannel destinationChannel = null;
        try {
            sourceChannel = new FileInputStream(source).getChannel();
            destinationChannel = new FileOutputStream(destination).getChannel();
            long count = 0;
            long size = sourceChannel.size();
            while ((count += destinationChannel.transferFrom(sourceChannel, 0, size - count)) < size) ;
        } finally {
            if (sourceChannel != null) sourceChannel.close();
            if (destinationChannel != null) destinationChannel.close();
        }
    }
