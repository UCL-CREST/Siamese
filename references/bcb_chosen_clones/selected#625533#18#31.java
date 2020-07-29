    public void backup(File source) throws BackupException {
        try {
            int index = source.getAbsolutePath().lastIndexOf(".");
            if (index == -1) return;
            File dest = new File(source.getAbsolutePath().substring(0, index) + ".bak");
            FileChannel srcChannel = new FileInputStream(source).getChannel();
            FileChannel dstChannel = new FileOutputStream(dest).getChannel();
            dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
            srcChannel.close();
            dstChannel.close();
        } catch (Exception ex) {
            throw new BackupException(ex.getMessage(), ex, source);
        }
    }
