    public void exportDeck() {
        if (theDeckVector.isEmpty()) return;
        String path = "";
        fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = fc.showSaveDialog(framer);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            if (Filer.fileExists(file)) {
                int answer = JOptionPane.showConfirmDialog(framer, "File Already Exists, Overwrite?", "File Exists", JOptionPane.YES_NO_OPTION);
                if (answer != JOptionPane.YES_OPTION) exportDeck();
            }
            path = file.getAbsolutePath();
            Filer.saveCardList(path, new ArrayList<Card>(theDeckVector));
        }
    }
