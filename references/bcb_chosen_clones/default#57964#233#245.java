    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("Exit")) System.exit(-1); else if (command.equals("knownauthor")) currentAuthor.setEnabled(true); else if (command.equals("unknownauthor")) currentAuthor.setEnabled(false); else if (command.equals("Import")) {
            System.out.println(buttonGroup1.getSelection().getActionCommand());
            int returnVal = chooseFile.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = chooseFile.getSelectedFile();
                System.out.println(file);
            }
        } else if (command.equals("demo")) {
            loadDemo();
        } else System.out.println(command);
    }
