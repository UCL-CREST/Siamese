    public void importDeck() {
        fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setFileFilter(new FileNameExtensionFilter(null, Filer.FILE_EXT));
        fc.setMultiSelectionEnabled(true);
        int returnVal = fc.showOpenDialog(framer);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File[] files = fc.getSelectedFiles();
            if (files.length < 1) return;
            String[] paths = new String[files.length];
            for (int i = 0; i < files.length; i++) {
                paths[i] = files[i].getAbsolutePath();
            }
            importDecks(paths);
        }
        fc.setMultiSelectionEnabled(false);
    }
