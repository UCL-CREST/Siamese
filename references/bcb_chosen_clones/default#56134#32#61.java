    public void importFlame() {
        try {
            Debugger.appendAction("Starting importFlame");
            JFileChooser pickFile = new JFileChooser();
            pickFile.setCurrentDirectory(new File(Settings.lastImportedFolder));
            pickFile.setFileFilter(new FlameFilter());
            pickFile.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int retVal = pickFile.showOpenDialog(main);
            if (retVal == JFileChooser.APPROVE_OPTION) {
                try {
                    FlameSelector selector = new FlameSelector(pickFile.getSelectedFile(), this, main, renderQueue);
                    if (selector.hasFlame()) {
                        prevFlames = flames.toArray();
                        flames.add(selector.getFlame());
                        main.refreshThumbs();
                        Settings.lastImportedFolder = pickFile.getCurrentDirectory().toString();
                        Settings.saveSettings();
                        Debugger.appendLog("Succesfull: importFlame");
                    }
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(main, "Invalid .flame file", "Error opening file", JOptionPane.ERROR_MESSAGE);
                    Debugger.appendLog("Failed: importFlame");
                    Debugger.storeException(ex);
                }
            }
            Debugger.appendAction("Ending importFlame");
        } catch (NullPointerException ex) {
            Debugger.storeException(ex);
        }
    }
