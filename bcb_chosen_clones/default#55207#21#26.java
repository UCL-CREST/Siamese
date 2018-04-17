    public void actionPerformed(ActionEvent evt) {
        int returnValue = chooser.showOpenDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            this.dataFrame.setFile(chooser.getSelectedFile());
        }
    }
