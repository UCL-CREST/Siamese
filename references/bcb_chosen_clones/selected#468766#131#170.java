    private synchronized Frame insertFrame(int index, File source, INSERT_TYPE type) throws IOException {
        if (source == null) throw new NullPointerException("Parameter 'source' is null");
        if (!source.exists()) throw new IOException("File does not exist: " + source.getAbsolutePath());
        if (source.length() <= 0) throw new IOException("File is empty: " + source.getAbsolutePath());
        if (index < 0) throw new IndexOutOfBoundsException("index < 0");
        if (index >= frames_.size()) throw new IndexOutOfBoundsException("index >= frames_.size()");
        File tmp = new File(Settings.getPropertyString(ConstantKeys.project_dir), "tmp.jpg");
        switch(type) {
            case MOVE:
                if (source.getParentFile().compareTo(new File(Settings.getPropertyString(ConstantKeys.project_dir))) == 0 && source.getName().matches("img_[0-9]{5}\\.jpg")) {
                    for (int i = 0; i < frames_.size(); i++) {
                        Frame f = frames_.get(i);
                        if (f.getFile().compareTo(source) == 0) {
                            frames_.remove(i);
                            break;
                        }
                    }
                }
                source.renameTo(tmp);
                break;
            case COPY:
                FileChannel inChannel = new FileInputStream(source).getChannel();
                FileChannel outChannel = new FileOutputStream(tmp).getChannel();
                inChannel.transferTo(0, inChannel.size(), outChannel);
                if (inChannel != null) inChannel.close();
                if (outChannel != null) outChannel.close();
                break;
        }
        for (int i = frames_.size() - 1; i >= index; i--) {
            Frame newFrame = new Frame(new File(Settings.getPropertyString(ConstantKeys.project_dir), formatFileName(i)));
            frames_.get(i).moveTo(newFrame);
            frames_.set(i, newFrame);
        }
        File newLocation = new File(Settings.getPropertyString(ConstantKeys.project_dir), formatFileName(index));
        tmp.renameTo(newLocation);
        Frame f = new Frame(newLocation);
        f.createThumbNail();
        frames_.set(index, f);
        return f;
    }
