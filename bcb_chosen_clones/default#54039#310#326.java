    private void openFastaFileActionPerformed(java.awt.event.ActionEvent evt) {
        if (evt.getSource() == openFastaFile) {
            int returnVal = fc.showOpenDialog(ParameterSolutions.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                inputFile = fc.getSelectedFile();
                log.append("Opening: " + inputFile.getName() + "\n");
                if (!inputFile.canRead() || !BinningFasta.verifyInputFile(inputFile)) {
                    log.append("That is not a valid fasta file, please choose  a properly formatted fasta file.\n");
                    inputFile = null;
                    return;
                }
                narr.println("Opening: " + inputFile.getName());
            } else {
                log.append("Dialog Cancelled by User.\n");
            }
        }
    }
