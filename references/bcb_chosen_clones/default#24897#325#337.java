    public void importListDialog() {
        JFileChooser oracle = new JFileChooser();
        String[] vars = { ".list", ".LIST", "List" };
        oracle.addChoosableFileFilter(new scribeFilter(vars));
        oracle.setAcceptAllFileFilterUsed(false);
        if (oracle.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            temp = oracle.getSelectedFile().getPath();
            topicList.setListData(lincoln.importList(temp));
            topicList.setSelectedIndex(0);
            textArea.setText(temp = new String(lincoln.readNoteBody(0)));
        }
        boogie();
    }
