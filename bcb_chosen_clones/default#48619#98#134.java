    private void runAllActionPerformed(java.awt.event.ActionEvent evt) {
        if (evt.getSource() == runAll) {
            int returnVal = fc.showOpenDialog(PMCopy.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                inputFile = fc.getSelectedFile();
                if (!BinningFasta.verifyInputFile(inputFile)) {
                    log.append("That is not a valid fasta file, please choose" + " a properly formatted fasta file.\n");
                    return;
                }
                (new Thread() {

                    public void run() {
                        log.append("Opening: " + inputFile.getName() + "\n");
                        narr.println("Opening: " + inputFile.getName() + "\n");
                        File startingFile = removeOutgroup(inputFile);
                        int userChoice = userSortPercentage();
                        values = new BinningAndFred(startingFile, log, narr, userChoice);
                        values.run();
                        hClimbResult = null;
                        try {
                            hClimbResult = hillClimbing(values.getValue());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        runNpopConfidenceInterval();
                        runSigmaConfidenceInterval();
                        runOmegaConfidenceInterval();
                        runDriftConfidenceInterval();
                        readyForCI = true;
                        printResults();
                    }
                }).start();
            } else {
                log.append("Dialog Cancelled by User.\n");
            }
        }
    }
