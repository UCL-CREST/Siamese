    private void openItemActionPerformed(java.awt.event.ActionEvent evt) {
        if (evt.getSource() == openItem) {
            int returnVal = fc.showOpenDialog(Demarcations.this);
            treeFile = null;
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                inputFile = fc.getSelectedFile();
                if (!BinningFasta.verifyInputFile(inputFile)) {
                    log.append("That is not a valid fasta file, please choose" + " a properly formatted fasta file.\n");
                    return;
                }
                (new Thread() {

                    public void run() {
                        runTree();
                    }
                }).start();
            } else {
                log.append("Dialog Cancelled by User.\n");
            }
        }
    }
