    public void youSure() {
        Object[] choices = { "Save", "Discard", "Cancel" };
        int choice = JOptionPane.showOptionDialog(null, "Are you sure you want to quit?", "Are you sure?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
        if (choice == 0) {
            int returnVal = chooser.showSaveDialog(GUI.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                fileio.write(textArea.getText(), file.getPath());
            }
            System.exit(1);
        } else if (choice == 1) {
            System.exit(1);
        }
    }
