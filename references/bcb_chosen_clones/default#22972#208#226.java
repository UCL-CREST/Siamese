    private void doBrowseDirectory() {
        JFileChooser browser = new JFileChooser();
        String str = OStrings.SW_BUTTON_SELECT;
        browser.setApproveButtonText(str);
        browser.setDialogTitle(OStrings.SW_TITLE);
        browser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        String curDir = m_dirField.getText();
        if (curDir.equals("") == false) {
            File dir = new File(curDir);
            if (dir.exists() && dir.isDirectory()) {
                browser.setCurrentDirectory(dir);
            }
        }
        int res = browser.showOpenDialog(this);
        File dir = browser.getSelectedFile();
        if (dir == null) return;
        str = dir.getAbsolutePath() + File.separator;
        m_dirField.setText(str);
    }
