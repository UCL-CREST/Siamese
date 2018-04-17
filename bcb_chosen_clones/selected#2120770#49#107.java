    public void run() {
        if (name == null) {
            System.err.println("Must set file name for retrieval");
            return;
        }
        if (handle == null) {
            System.err.println("Must set CCNHandle");
            return;
        }
        if (htmlPane == null) {
            System.err.println("Must set JEditorPane");
            return;
        }
        JFrame frame = new JFrame();
        JFileChooser chooser = new JFileChooser();
        File f = null;
        chooser.setCurrentDirectory(null);
        int returnVal = chooser.showSaveDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            f = chooser.getSelectedFile();
        } else {
            htmlPane.setText("Save File cancelled");
            return;
        }
        boolean overwrite = false;
        try {
            if (f.exists()) {
                htmlPane.setText("Overwriting contents of " + f.getPath());
                overwrite = true;
            } else {
                f.createNewFile();
            }
            if (f.canWrite()) {
            } else {
                htmlPane.setText("The ContentExplorer is unable to write the content to the specified file.");
                return;
            }
        } catch (IOException e) {
            System.err.println("could not create " + f.getPath() + " for saving content to filesystem");
            htmlPane.setText("could not create " + f.getPath() + " for saving content to filesystem");
        }
        try {
            if (!overwrite) htmlPane.setText("saving " + name + " to " + f.getCanonicalPath()); else htmlPane.setText("overwriting contents of " + f.getCanonicalPath() + " to save " + name);
            CCNFileInputStream fis = new CCNFileInputStream(name, handle);
            FileOutputStream output = new FileOutputStream(f);
            byte[] buffer = new byte[readsize];
            int readcount = 0;
            int readtotal = 0;
            while ((readcount = fis.read(buffer)) != -1) {
                readtotal += readcount;
                output.write(buffer, 0, readcount);
                output.flush();
            }
            htmlPane.setText("Saved " + name + " to " + f.getCanonicalPath());
        } catch (Exception e) {
            htmlPane.setText("Could not save " + name + " to " + f.getPath() + " This may be a prefix for an object or just may not be available at this time.");
            System.err.println("Could not retrieve file: " + name);
        }
    }
