    private void copyFile(File source) throws IOException {
        File backup = new File(source.getCanonicalPath() + ".backup");
        if (!backup.exists()) {
            FileChannel srcChannel = new FileInputStream(source).getChannel();
            backup.createNewFile();
            FileChannel dstChannel = new FileOutputStream(backup).getChannel();
            dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
            srcChannel.close();
            dstChannel.close();
        }
    }
