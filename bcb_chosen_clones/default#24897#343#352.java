    public void exportListDialog() {
        JFileChooser oracle = new JFileChooser();
        String[] vars = { ".list", ".LIST", "List" };
        oracle.addChoosableFileFilter(new scribeFilter(vars));
        oracle.setAcceptAllFileFilterUsed(false);
        if (oracle.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            temp = oracle.getSelectedFile().getPath();
            lincoln.exportList(temp);
        }
    }
