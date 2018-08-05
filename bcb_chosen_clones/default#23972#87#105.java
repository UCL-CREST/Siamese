        private File promptFile() throws FileNotFoundException {
            File file;
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Open Shapefile Directory");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setFileFilter(new FileFilter() {

                public boolean accept(File f) {
                    return f.isDirectory();
                }

                public String getDescription() {
                    return "Directory";
                }
            });
            chooser.showOpenDialog(null);
            file = chooser.getSelectedFile();
            return file;
        }
