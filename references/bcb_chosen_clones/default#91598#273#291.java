    private void funcOeffnen() {
        fileDialog.resetChoosableFileFilters();
        fileDialog.addChoosableFileFilter(new FileNameExtensionFilter("NMEA-Logs (*.txt)", "txt"));
        fileDialog.addChoosableFileFilter(new FileNameExtensionFilter("GPS Exchange Format (*.gpx)", "gpx"));
        fileDialog.addChoosableFileFilter(new FileNameExtensionFilter("GPS-Tracks (*.txt),(*.gpx)", "txt", "gpx"));
        int state = fileDialog.showOpenDialog(null);
        String file = null;
        if (state == JFileChooser.APPROVE_OPTION) {
            file = fileDialog.getSelectedFile().getPath();
        }
        if (file != null) {
            if (wflw.open(file)) {
                textfeld.append("\"" + file + "\" erfolgreich geladen\n");
                textfeld.append(wflw.anzKoordinaten() + " Koordinaten-Datens�tze\n");
                setAllEnabled();
            }
        }
        funcDrawMap();
    }
