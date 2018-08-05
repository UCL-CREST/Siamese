    private void doSaveCommand() {
        fileChooser.setSelectedFile(new File("MyDrawing.mpov"));
        int returnVal = fileChooser.showSaveDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                String filename = fileChooser.getSelectedFile().getCanonicalPath();
                FileOutputStream fout = new FileOutputStream(filename);
                ObjectOutputStream oout = new ObjectOutputStream(fout);
                oout.writeObject(columns);
                oout.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Could not write the file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
