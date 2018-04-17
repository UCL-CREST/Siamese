    void guiOpenTheFile() {
        oooeye.apend("3");
        int sod = 0;
        File file = null;
        boolean gui = true;
        JFileChooser jFchooser = new JFileChooser();
        sod = jFchooser.showOpenDialog(this);
        file = jFchooser.getSelectedFile();
        jTextArea.append("Opening " + file + " ...\n");
        Pgn pgn = new Pgn();
        pgn.parsePgn(file, oooeye, gui);
        jTextArea.append("\n");
    }
