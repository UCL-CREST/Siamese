    public static final void copy(String source, String destination) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(source);
            fos = new FileOutputStream(destination);
            java.nio.channels.FileChannel channelSrc = fis.getChannel();
            java.nio.channels.FileChannel channelDest = fos.getChannel();
            channelSrc.transferTo(0, channelSrc.size(), channelDest);
            fis.close();
            fos.close();
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }
