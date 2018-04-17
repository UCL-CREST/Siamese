    private void SaveASGraph() {
        JFileChooser chooser = new JFileChooser(DirG);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        ExtensionFileFilter filter = new ExtensionFileFilter("grf", "GRAPH representation files (*.grf)");
        chooser.setFileFilter(filter);
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        DirG = chooser.getSelectedFile().getParent();
        PathG = chooser.getSelectedFile().getPath();
        TabG = chooser.getSelectedFile().getName();
        if (!PathG.endsWith(".grf")) {
            PathG = PathG + ".grf";
            TabG = TabG + ".grf";
        }
        SaveGraph();
    }
