    private void openButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (evt.getSource() == openButton) {
            int returnVal = fc.showOpenDialog(PMCopy.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                inputFile = fc.getSelectedFile();
                if (!BinningFasta.verifyInputFile(inputFile)) {
                    log.append("That is not a valid fasta file, please choose" + " a properly formatted fasta file.\n");
                    return;
                }
                thread = new Thread(this);
                thread.start();
            } else {
                log.append("Dialog Cancelled by User.\n");
            }
        }
    }
