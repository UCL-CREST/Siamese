    public static void copyFile(File file, String destDir) throws IOException {
        if (!isCanReadFile(file)) throw new RuntimeException("The File can't read:" + file.getPath());
        if (!isCanWriteDirectory(destDir)) throw new RuntimeException("The Directory can't write:" + destDir);
        FileChannel srcChannel = null;
        FileChannel dstChannel = null;
        try {
            srcChannel = new FileInputStream(file).getChannel();
            dstChannel = new FileOutputStream(destDir + "/" + file.getName()).getChannel();
            dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
        } catch (IOException e) {
            throw e;
        } finally {
            if (srcChannel != null) try {
                srcChannel.close();
            } catch (IOException e) {
            }
            if (dstChannel != null) try {
                dstChannel.close();
            } catch (IOException e) {
            }
        }
    }
