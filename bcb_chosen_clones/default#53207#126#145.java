    String[] askForDirectory() {
        JOptionPane.showMessageDialog(this, "parameters missing:\n[properies-file] <directory1> ...", "JSlideShow", JOptionPane.ERROR_MESSAGE);
        final JFileChooser fc = new JFileChooser();
        fc.setMultiSelectionEnabled(true);
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        final int returnVal = fc.showOpenDialog(null);
        if (returnVal != JFileChooser.APPROVE_OPTION) {
            System.exit(0);
        }
        final File[] fList = fc.getSelectedFiles();
        final Vector<String> ret = new Vector<String>();
        for (File name : fList) {
            try {
                ret.addElement(name.getCanonicalPath());
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        return (String[]) ret.toArray();
    }
