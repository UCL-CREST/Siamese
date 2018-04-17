    public void newCrutch() {
        JFileChooser oracle = new JFileChooser();
        String[] vars = { ".crutch", ".CRUTCH", "Crutch" };
        oracle.addChoosableFileFilter(new scribeFilter(vars));
        oracle.setAcceptAllFileFilterUsed(false);
        if (oracle.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            filename[0] = oracle.getSelectedFile().getPath();
            filename[1] = oracle.getSelectedFile().getName();
            String tempName = JOptionPane.showInputDialog(null, "Enter a title for your crutch:", "Enter crutch title", JOptionPane.QUESTION_MESSAGE);
            if (tempName != null) {
                lincoln.setTitle(tempName);
            }
            if (filename[0] != null) {
                saveCrutch(filename[0]);
                lincoln.clean();
                textArea.setText("");
            }
        }
        boogie();
    }
