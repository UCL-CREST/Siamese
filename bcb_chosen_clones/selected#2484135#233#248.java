    public static void writeFileToFile(File fin, File fout, boolean append) throws IOException {
        FileChannel inChannel = new FileInputStream(fin).getChannel();
        FileChannel outChannel = new FileOutputStream(fout, append).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null) try {
                inChannel.close();
            } catch (IOException ex) {
            }
            if (outChannel != null) try {
                outChannel.close();
            } catch (IOException ex) {
            }
        }
    }
