    public static void copyDirs(File sourceDir, File destDir) throws IOException {
        if (!destDir.exists()) destDir.mkdirs();
        for (File file : sourceDir.listFiles()) {
            if (file.isDirectory()) {
                copyDirs(file, new File(destDir, file.getName()));
            } else {
                FileChannel srcChannel = new FileInputStream(file).getChannel();
                File out = new File(destDir, file.getName());
                out.createNewFile();
                FileChannel dstChannel = new FileOutputStream(out).getChannel();
                dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
                srcChannel.close();
                dstChannel.close();
            }
        }
    }
