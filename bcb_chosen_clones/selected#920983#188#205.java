    static void copyFile(File in, File outDir, String outFileName) throws IOException {
        FileChannel inChannel = new FileInputStream(in).getChannel();
        outDir.mkdirs();
        File outFile = new File(outDir, outFileName);
        FileChannel outChannel = new FileOutputStream(outFile).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } catch (IOException e) {
            throw e;
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }
