    public void actionPerformed(ActionEvent e) {
        if (bopen == e.getSource()) {
            if (JFileChooser.APPROVE_OPTION == openDialog.showOpenDialog(this)) openSession(openDialog.getSelectedFile());
        } else if (bok == e.getSource()) {
            okExec();
        } else if (bmacros == e.getSource()) {
            macros.setVisible(true);
        }
    }
