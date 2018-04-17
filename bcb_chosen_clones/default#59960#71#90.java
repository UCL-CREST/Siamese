    private void writeNormalButtonActionPerformed(java.awt.event.ActionEvent evt) {
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fc.setDialogTitle("Choose a Directory and FileName");
        fc.setApproveButtonText("Save File As");
        String filePath = input.getPath();
        fc.setSelectedFile(new File(filePath));
        int returnVal = fc.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            saveFile = fc.getSelectedFile();
        } else return;
        try {
            PrintWriter normalOut = new PrintWriter(saveFile, "UTF-8");
            String contents = jTextPane1.getText();
            normalOut.println(contents);
            normalOut.flush();
            normalOut.close();
        } catch (Exception ex) {
            String oops = ex.toString();
        }
    }
