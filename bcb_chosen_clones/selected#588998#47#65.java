    private static void copyFiles(String strPath, String dstPath) throws Exception {
        File src = new File(strPath);
        File dest = new File(dstPath);
        if (src.isDirectory()) {
            dest.mkdirs();
            String list[] = src.list();
            for (int i = 0; i < list.length; i++) {
                String dest1 = dest.getAbsolutePath() + "\\" + list[i];
                String src1 = src.getAbsolutePath() + "\\" + list[i];
                copyFiles(src1, dest1);
            }
        } else {
            FileChannel sourceChannel = new FileInputStream(src).getChannel();
            FileChannel targetChannel = new FileOutputStream(dest).getChannel();
            sourceChannel.transferTo(0, sourceChannel.size(), targetChannel);
            sourceChannel.close();
            targetChannel.close();
        }
    }
