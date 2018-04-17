    private synchronized Frame addFrame(INSERT_TYPE type, File source) throws IOException {
        if (source == null) throw new NullPointerException("Parameter 'source' is null");
        if (!source.exists()) throw new IOException("File does not exist: " + source.getAbsolutePath());
        if (source.length() <= 0) throw new IOException("File is empty: " + source.getAbsolutePath());
        File newLocation = new File(Settings.getPropertyString(ConstantKeys.project_dir), formatFileName(frames_.size()));
        if (newLocation.compareTo(source) != 0) {
            switch(type) {
                case MOVE:
                    source.renameTo(newLocation);
                    break;
                case COPY:
                    FileChannel inChannel = new FileInputStream(source).getChannel();
                    FileChannel outChannel = new FileOutputStream(newLocation).getChannel();
                    inChannel.transferTo(0, inChannel.size(), outChannel);
                    if (inChannel != null) inChannel.close();
                    if (outChannel != null) outChannel.close();
                    break;
            }
        }
        Frame f = new Frame(newLocation);
        f.createThumbNail();
        frames_.add(f);
        return f;
    }
