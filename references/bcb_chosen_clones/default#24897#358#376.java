    public void exportDialog(String command) {
        JFileChooser oracle = new JFileChooser();
        String[] vars;
        if (command.equals("Html")) {
            vars = new String[] { ".html", ".HTML", "Html" };
        } else if (command.equals("Text")) {
            vars = new String[] { ".txt", ".TXT", "Text" };
        } else if (command.equals("Wave")) {
            vars = new String[] { ".wav", ".WAV", "Wave" };
        } else {
            vars = new String[] { ".mp3", ".MP3", "Mp3" };
        }
        oracle.addChoosableFileFilter(new scribeFilter(vars));
        oracle.setAcceptAllFileFilterUsed(false);
        if (oracle.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            exportCrutch(command, oracle.getSelectedFile().getPath());
        }
        boogie();
    }
