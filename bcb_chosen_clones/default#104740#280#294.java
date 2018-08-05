    private void SaveASSchedule() {
        JFileChooser chooser = new JFileChooser(DirS);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        ExtensionFileFilter filter = new ExtensionFileFilter("sch", "Schedule graph representation files (*.sch)");
        chooser.setFileFilter(filter);
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        DirS = chooser.getSelectedFile().getParent();
        PathS = chooser.getSelectedFile().getPath();
        TabS = chooser.getSelectedFile().getName();
        if (!PathS.endsWith(".sch")) {
            PathS = PathS + ".sch";
            TabS = TabS + ".sch";
        }
        SaveSchedule();
    }
