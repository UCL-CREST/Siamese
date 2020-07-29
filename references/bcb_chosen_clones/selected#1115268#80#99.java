    public static void copyFile(File in, File out) {
        try {
            FileChannel inChannel = null, outChannel = null;
            try {
                out.getParentFile().mkdirs();
                inChannel = new FileInputStream(in).getChannel();
                outChannel = new FileOutputStream(out).getChannel();
                outChannel.transferFrom(inChannel, 0, inChannel.size());
            } finally {
                if (inChannel != null) {
                    inChannel.close();
                }
                if (outChannel != null) {
                    outChannel.close();
                }
            }
        } catch (Exception e) {
            ObjectUtils.throwAsError(e);
        }
    }
