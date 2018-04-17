    public void doSplash() {
        final JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileFilter() {

            public boolean accept(final File f) {
                final String name = f.getName().toLowerCase();
                final int dotPos = name.lastIndexOf('.');
                final String ext = -1 == dotPos ? "" : name.substring(dotPos + 1);
                return f.isDirectory() || ext.equals("gif") || ext.equals("png") || ext.equals("jpg") || ext.equals("jpeg");
            }

            public String getDescription() {
                return "Image files";
            }
        });
        if (JFileChooser.APPROVE_OPTION == chooser.showOpenDialog(x_main_extraParams)) addOption("\"-splash:" + chooser.getSelectedFile().getAbsolutePath() + '"');
    }
