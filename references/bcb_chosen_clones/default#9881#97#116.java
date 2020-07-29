    private void saveFile() {
        JFileChooser fi = new JFileChooser();
        String theLine = "";
        fi.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fi.showSaveDialog(this);
        if (result == JFileChooser.CANCEL_OPTION) return;
        File fina = fi.getSelectedFile();
        if (fina == null || fina.getName().equals("")) JOptionPane.showMessageDialog(this, "Invalid File Name", "Error", JOptionPane.ERROR_MESSAGE); else {
            try {
                FileWriter out = new FileWriter(fina);
                String text = t.getText();
                out.write(text);
                p = 1;
                out.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error Writting the File", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        filepath = fina.getPath();
    }
