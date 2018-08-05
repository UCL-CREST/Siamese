    private void SaveAsSchedule() {
        JFileChooser chooser = new JFileChooser("./");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        cxExtFileFilter filter = new cxExtFileFilter("sch", "SCHEDULE representation files (*.sch)");
        chooser.setFileFilter(filter);
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        setTitle("gxTool Schedule - [" + chooser.getSelectedFile().getPath() + "]");
        SaveSchedule(chooser.getSelectedFile().getPath());
    }
