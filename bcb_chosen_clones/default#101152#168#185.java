    public File[] doFilesOpen() {
        JFileChooser chooser = new JFileChooser();
        ArchiveFilter filter = new ArchiveFilter();
        filter.addExtension("jar");
        filter.addExtension("zip");
        filter.setDescription("Jar & Zip Files");
        File root = new File("/fuegotech3.1.3");
        chooser.setCurrentDirectory(root);
        chooser.setMultiSelectionEnabled(true);
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(_getFrame(this));
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFiles();
        } else {
            return null;
        }
    }
