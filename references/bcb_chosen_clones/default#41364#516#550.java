    private void saveAs() {
        File file;
        JFileChooser jFileChooser;
        jFileChooser = new JFileChooser();
        jFileChooser.addChoosableFileFilter(new ModelFileFilter());
        jFileChooser.setCurrentDirectory(path);
        for (; ; ) {
            if (jFileChooser.showSaveDialog(getJFrame()) != JFileChooser.APPROVE_OPTION) {
                statusLabel.setForeground(Color.black);
                statusLabel.setText("File save aborted by user");
                return;
            }
            try {
                file = new File(jFileChooser.getSelectedFile().getCanonicalPath());
                if (!file.exists() && jFileChooser.getFileFilter() instanceof ModelFileFilter && !file.getName().contains(".")) {
                    file = new File(file.getAbsolutePath() + ".mod");
                }
                if (file.exists()) {
                    switch(JOptionPane.showConfirmDialog(jFrame, "Replace existing file?")) {
                        case JOptionPane.NO_OPTION:
                            continue;
                        case JOptionPane.CANCEL_OPTION:
                            statusLabel.setForeground(Color.black);
                            statusLabel.setText("File save aborted by user");
                            return;
                    }
                }
                writeFile(file);
                return;
            } catch (IOException ex) {
                statusLabel.setForeground(Color.red);
                statusLabel.setText(ex.getMessage());
            }
        }
    }
