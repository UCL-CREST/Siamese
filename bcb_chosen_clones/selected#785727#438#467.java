    public static void copyFileNIO(File in, File out) throws IOException {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(in);
            fos = new FileOutputStream(out);
            FileChannel sourceChannel = fis.getChannel();
            FileChannel targetChannel = fos.getChannel();
            sourceChannel.transferTo(0, sourceChannel.size(), targetChannel);
            sourceChannel.close();
            targetChannel.close();
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
    }
