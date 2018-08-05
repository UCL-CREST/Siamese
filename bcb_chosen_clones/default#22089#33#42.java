    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == scanButton) {
            JOptionPane.showMessageDialog(this, "Not implemented yet. Sorry!", "Scanning not implemented", JOptionPane.WARNING_MESSAGE);
        } else if (e.getSource() == loadButton) {
            int returnval = fc.showOpenDialog(this);
            if (returnval == JFileChooser.APPROVE_OPTION) dpane.loadFile(fc.getSelectedFile());
        } else if (e.getSource() == beamButton) {
            dpane.applyBeam();
        }
    }
