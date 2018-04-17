    public void actionPerformed(ActionEvent e) {
        File suggest = null;
        if (fileName != null) suggest = new File(fileName);
        Options.saveFileChooser.setSelectedFile(suggest);
        int returnVal = Options.saveFileChooser.showSaveDialog(parent);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = Options.saveFileChooser.getSelectedFile();
            try {
                Utilities.copyStream(new BufferedInputStream(input), new BufferedOutputStream(new FileOutputStream(file)));
            } catch (Exception ex) {
                LogFrame.log(ex);
            }
        }
    }
