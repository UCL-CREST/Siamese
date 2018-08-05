    protected void saveClassifier() {
        JFileChooser chooser = new JFileChooser();
        try {
            chooser.setCurrentDirectory(new File(new File(".").getCanonicalPath()));
        } catch (IOException ioe) {
        }
        int returnVal = chooser.showSaveDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            if (classifiers.size() < 1) return;
            classifiers.get(INDEX_RUBINE).save(chooser.getSelectedFile());
        }
    }
