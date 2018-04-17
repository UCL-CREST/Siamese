    private File waehleDatei(String msg) {
        JFileChooser fc = new JFileChooser();
        fc.setApproveButtonText(msg);
        int returnVal = fc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) return fc.getSelectedFile(); else return null;
    }
