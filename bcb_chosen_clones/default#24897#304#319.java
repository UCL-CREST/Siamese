    public void restoreDialog() {
        JFileChooser oracle = new JFileChooser();
        String[] vars = { ".crutch", ".CRUTCH", "Crutch" };
        oracle.addChoosableFileFilter(new scribeFilter(vars));
        oracle.setAcceptAllFileFilterUsed(false);
        if (oracle.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            filename[0] = oracle.getSelectedFile().getPath();
            filename[1] = oracle.getSelectedFile().getName();
            textArea.setEditable(true);
            lincoln.restoreBinder(filename[0]);
            textArea.setText(temp = new String(lincoln.readNoteBody(0)));
            topicList.setSelectedIndex(0);
            currentTopic = 0;
            boogie();
        }
    }
