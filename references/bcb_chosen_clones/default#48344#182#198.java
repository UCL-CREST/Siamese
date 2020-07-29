    public String getFile(int type) {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter;
        if (type == 0) {
            chooser.setCurrentDirectory(new File(gameProperties.getProperty("skin.folder") + "\\"));
            filter = new FileNameExtensionFilter("config files", "config");
        } else {
            chooser.setCurrentDirectory(new File(gameProperties.getProperty("map.folder") + "\\"));
            filter = new FileNameExtensionFilter("map files", "map");
        }
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return chooser.getCurrentDirectory().toString() + "\\" + chooser.getSelectedFile().getName();
        }
        return null;
    }
