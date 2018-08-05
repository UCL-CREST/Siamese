    public void saveAs() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.addChoosableFileFilter(new VESTChartFilter());
        fileChooser.setCurrentDirectory(new File("."));
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.CANCEL_OPTION) return;
        File fileName = fileChooser.getSelectedFile();
        if (fileName == null || fileName.getName().equals("")) {
            JOptionPane.showMessageDialog(this, "Invalid File Name", "Invalid File Name", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                output = new ObjectOutputStream(new FileOutputStream(fileName));
                save();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error Saving File", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
