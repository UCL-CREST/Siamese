    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("Exit")) System.exit(-1); else if (command.equals("knownauthor")) {
            currentAuthor.setText("");
            currentAuthor.setEnabled(true);
        } else if (command.equals("unknownauthor")) {
            currentAuthor.setText("");
            currentAuthor.setEnabled(false);
        } else if (command.equals("Import")) {
            String fcomp = new String();
            System.out.println(buttonGroup1.getSelection().getActionCommand());
            int returnVal = chooseFile.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = chooseFile.getSelectedFile();
                driver.addDocument(file.toString(), currentAuthor.getText());
                if (!currentAuthor.getText().equals("")) listKnown.append(currentAuthor.getText() + " - " + file.getName() + "\n"); else listUnknown.append(file.getName() + "\n");
                System.out.println(file);
            }
        } else if (command.equals("Canonicize")) {
            driver.preprocessEngine(canonicizers);
        } else if (command.equals("CreateEventSet")) {
            driver.createEventSet(esButtonGroup.getSelection().getActionCommand());
        } else if (command.equals("Analyze")) {
            String results = driver.runStatisticalAnalysis(buttonGroup2.getSelection().getActionCommand());
            listResults.append(results);
        } else if (command.equals("demo")) {
            loadDemo();
        } else System.out.println(command);
    }
