    public static void copyFile(File in, File out, boolean read, boolean write, boolean execute) throws FileNotFoundException, IOException {
        FileChannel inChannel = new FileInputStream(in).getChannel();
        File outFile = null;
        if (out.isDirectory()) {
            outFile = new File(out.getAbsolutePath() + File.separator + in.getName());
        } else {
            outFile = out;
        }
        FileChannel outChannel = new FileOutputStream(outFile).getChannel();
        try {
            int maxCount = (64 * 1024 * 1024) - (32 * 1024);
            long size = inChannel.size();
            long position = 0;
            while (position < size) {
                position += inChannel.transferTo(position, maxCount, outChannel);
            }
            outFile.setReadable(read);
            outFile.setWritable(write);
            outFile.setExecutable(execute);
        } catch (IOException e) {
            throw e;
        } finally {
            if (inChannel != null) inChannel.close();
            if (outChannel != null) outChannel.close();
        }
    }
