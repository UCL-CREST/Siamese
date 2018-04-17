    private void funcSaveKML() {
        String filename = null;
        fileDialog.resetChoosableFileFilters();
        fileDialog.addChoosableFileFilter(new FileNameExtensionFilter("KML-GPS-Logs (*.kml)", "kml"));
        int state = fileDialog.showSaveDialog(null);
        if (state == JFileChooser.APPROVE_OPTION) filename = fileDialog.getSelectedFile().getPath();
        if (filename != null) Save.saveKML(filename, wflw.getPumpen(), wflw.getKoordList());
    }
