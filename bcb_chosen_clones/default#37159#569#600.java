    protected void doSaveAs() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileFilter() {

            public boolean accept(File file) {
                String filename = file.getName();
                if (file.isDirectory()) return true;
                return filename.endsWith(".col");
            }

            public String getDescription() {
                return "(*.col) ColorScheme files";
            }
        });
        int result = fileChooser.showSaveDialog(this);
        switch(result) {
            case JFileChooser.CANCEL_OPTION:
                System.out.println("openFileMenuItemMouseReleased() Cancel");
                break;
            case JFileChooser.ERROR_OPTION:
                System.out.println("openFileMenuItemMouseReleased() Error");
                break;
            case JFileChooser.APPROVE_OPTION:
                String selFile = fileChooser.getSelectedFile().getAbsolutePath();
                if (!selFile.toLowerCase().endsWith(".col")) {
                    selFile = selFile + ".col";
                }
                CousheApp.lastAccessedFilename = selFile;
                this.aoColorScheme.saveColorScheme(selFile);
                this.refreshAll();
        }
    }
