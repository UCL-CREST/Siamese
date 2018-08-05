    public boolean askSaveReplay() {
        JFrame frame = JLad.getMainWindow();
        String msg = "Do you want to save a replay of this game?";
        String title = "Save a replay?";
        int otype = JOptionPane.YES_NO_OPTION;
        int a = JOptionPane.showConfirmDialog(frame, msg, title, otype);
        if (a != JOptionPane.YES_OPTION) return false;
        JFileChooser chooser = new JFileChooser(Preferences.getReplayPath());
        ReplayFileFilter filter = new ReplayFileFilter();
        chooser.setFileFilter(filter);
        int v = chooser.showSaveDialog(this);
        if (v == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            String fn = file.toString();
            if (!fn.endsWith(".rpl")) {
                fn = fn.concat(".rpl");
                file = new File(fn);
            }
            Preferences.setReplayPath(chooser.getCurrentDirectory().getAbsolutePath());
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                this.gameGrid.saveReplay(writer);
                writer.close();
            } catch (IOException e) {
                Debug.msg("IOException while saving a replay: " + e.getMessage() + "\n");
                Dialogues.error("Could not save the replay!");
                return false;
            }
        }
        return true;
    }
