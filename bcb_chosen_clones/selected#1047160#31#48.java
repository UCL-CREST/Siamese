    public void merge(VMImage image, VMSnapShot another) throws VMException {
        if (path == null || another.getPath() == null) throw new VMException("EmuVMSnapShot is NULL!");
        logging.debug(LOG_NAME, "merge images  " + path + " and " + another.getPath());
        File target = new File(path);
        File src = new File(another.getPath());
        if (target.isDirectory() || src.isDirectory()) return;
        try {
            FileInputStream in = new FileInputStream(another.getPath());
            FileChannel inChannel = in.getChannel();
            FileOutputStream out = new FileOutputStream(path, true);
            FileChannel outChannel = out.getChannel();
            outChannel.transferFrom(inChannel, 0, inChannel.size());
            outChannel.close();
            inChannel.close();
        } catch (IOException e) {
            throw new VMException(e);
        }
    }
