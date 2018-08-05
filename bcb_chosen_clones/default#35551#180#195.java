    public void doOpen() {
        final JFileChooser chooser = new JFileChooser();
        chooser.addChoosableFileFilter(new FileFilter() {

            public boolean accept(final File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".log");
            }

            public String getDescription() {
                return "Log files";
            }
        });
        if (chooser.showOpenDialog(x_main) == JFileChooser.APPROVE_OPTION) {
            loadFile(chooser.getSelectedFile().getAbsolutePath(), true);
        }
    }
