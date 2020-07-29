    public File copyLocalFileAsTempFileInExternallyAccessableDir(String localFileRef) throws IOException {
        log.debug("copyLocalFileAsTempFileInExternallyAccessableDir");
        File f = this.createTempFileInExternallyAccessableDir();
        FileChannel srcChannel = new FileInputStream(localFileRef).getChannel();
        FileChannel dstChannel = new FileOutputStream(f).getChannel();
        log.debug("before transferring via FileChannel from src-inputStream: " + localFileRef + " to dest-outputStream: " + f.getAbsolutePath());
        dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
        srcChannel.close();
        dstChannel.close();
        log.debug("copyLocalFileAsTempFileInExternallyAccessableDir returning: " + f.getAbsolutePath());
        return f;
    }
