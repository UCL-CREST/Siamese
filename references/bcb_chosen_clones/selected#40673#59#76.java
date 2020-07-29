    public void copyNIO(File in, File out) throws IOException {
        FileInputStream inStream = null;
        FileOutputStream outStream = null;
        FileChannel sourceChannel = null;
        FileChannel destinationChannel = null;
        try {
            inStream = new FileInputStream(in);
            outStream = new FileOutputStream(out);
            sourceChannel = inStream.getChannel();
            destinationChannel = outStream.getChannel();
            destinationChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        } finally {
            if (sourceChannel != null) sourceChannel.close();
            if (destinationChannel != null) destinationChannel.close();
            if (inStream != null) inStream.close();
            if (outStream != null) outStream.close();
        }
    }
