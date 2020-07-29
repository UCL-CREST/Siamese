    private void doExportCommand() {
        fileChooser.setSelectedFile(new File("image_code.c"));
        int returnVal = fileChooser.showSaveDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                String filename = fileChooser.getSelectedFile().getCanonicalPath();
                FileWriter fw = new FileWriter(filename);
                fw.write(this.getImgCode());
                fw.close();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Could not write the file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
