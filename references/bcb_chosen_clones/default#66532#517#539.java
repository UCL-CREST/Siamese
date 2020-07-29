    public void showReplay() {
        if (this.inGame()) return;
        ReplayFileFilter filter = new ReplayFileFilter();
        String path = Preferences.getReplayPath();
        JFileChooser fileChooser = new JFileChooser();
        if (path != null) fileChooser = new JFileChooser(path); else fileChooser = new JFileChooser();
        fileChooser.setFileFilter(filter);
        int value = fileChooser.showOpenDialog(JLad.getMainWindow());
        if (value != JFileChooser.APPROVE_OPTION) return;
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileChooser.getSelectedFile()));
            Preferences.setReplayPath(fileChooser.getCurrentDirectory().getAbsolutePath());
            String line = br.readLine();
            br.close();
            this.initMode(this.REPLAY_MODE, new GridComponent(line));
        } catch (IOException e) {
            Debug.msg("IOException while loading a replay: " + e.getMessage() + "\n");
            Dialogues.error("Could not load the replay, read error!");
        } catch (BadReplayException e) {
            Debug.msg("BadReplayExceptin while loading a replay: " + e.getMessage() + "\n");
            Dialogues.error("Could not load the replay, parse error!");
        }
    }
