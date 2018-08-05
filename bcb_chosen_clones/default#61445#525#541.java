    public void importLibrary() {
        String path = "";
        fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = fc.showOpenDialog(framer);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            path = file.getAbsolutePath();
            app.importLibrary(path);
            filterLibrary();
            updateTheDeck();
            updateFilterTable();
            updateStateChanges();
            if (DEBUG) System.out.println("done!");
        }
        if (path.isEmpty()) return;
    }
