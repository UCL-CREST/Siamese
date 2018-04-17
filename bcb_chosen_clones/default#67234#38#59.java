    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == load) {
            JFileChooser lstchooser = new JFileChooser();
            lstchooser.setDialogTitle("Select file containing EEPROM memory data");
            if (lstchooser.showOpenDialog(Workbench.mainframe) == JFileChooser.APPROVE_OPTION) {
                File file = lstchooser.getSelectedFile();
                eeprom.loadbin(file.getPath());
                parent.updateDebugWindows();
            }
        }
        if (evt.getSource() == ok) {
            try {
                int data = Integer.parseInt(text.getText());
                if (data < 256) {
                    changer.setVisible(false);
                    mem.store(adress, data);
                    parent.updateDebugWindows();
                }
            } catch (NumberFormatException e) {
            }
        }
    }
