    public boolean loadExisting() throws IOException, InterruptedIOException {
        reset();
        JFileChooser d = new JFileChooser();
        int res = d.showOpenDialog(this);
        if (res == JFileChooser.CANCEL_OPTION) throw new InterruptedIOException();
        File fp = d.getSelectedFile();
        if (fp == null) return false; else return readProjFile(fp);
    }
