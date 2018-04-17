    private void openFile() {
        JFileChooser filech = new JFileChooser();
        String theLine = "";
        filech.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = filech.showOpenDialog(this);
        if (result == JFileChooser.CANCEL_OPTION) return;
        if (result == JFileChooser.APPROVE_OPTION) {
            File filename = filech.getSelectedFile();
            if (filename == null || filename.getName().equals("")) {
                JOptionPane.showMessageDialog(this, "Invalid File Name", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                int size = (int) filename.length();
                int char_reads = 0;
                FileReader in = new FileReader(filename);
                char data[] = new char[size];
                while (in.ready()) {
                    char_reads += in.read(data, char_reads, size - char_reads);
                }
                in.close();
                t.setText(new String(data, 0, char_reads));
            } catch (IOException ioe) {
                JOptionPane.showMessageDialog(this, "Error Openning the file", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return;
    }
