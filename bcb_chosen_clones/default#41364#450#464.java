    private void open() {
        File file;
        JFileChooser jFileChooser;
        jFileChooser = new JFileChooser();
        jFileChooser.addChoosableFileFilter(new ModelFileFilter());
        jFileChooser.setCurrentDirectory(path);
        if (jFileChooser.showOpenDialog(getJFrame()) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        file = jFileChooser.getSelectedFile();
        if (!file.exists() && jFileChooser.getFileFilter() instanceof ModelFileFilter && !file.getName().contains(".")) {
            file = new File(file.getAbsolutePath() + ".mod");
        }
        readFile(file);
    }
