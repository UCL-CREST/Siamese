            public void actionPerformed(ActionEvent evt) {
                File file;
                String os = System.getProperty("os.name");
                if (os != null && !os.startsWith("Linux")) {
                    FileDialog fc = new FileDialog(new Frame(), "Select file", FileDialog.LOAD);
                    fc.setVisible(true);
                    if (fc.getFile() == null) {
                        return;
                    }
                    String filename = fc.getDirectory() + fc.getFile();
                    file = new File(filename);
                } else {
                    JFileChooser fc = new JFileChooser();
                    int returnVal = fc.showOpenDialog(JFilePanel.this);
                    if (returnVal != JFileChooser.APPROVE_OPTION) {
                        return;
                    }
                    file = fc.getSelectedFile();
                }
                if (!file.canRead()) {
                    int choice = JOptionPane.showConfirmDialog(JFilePanel.this, "The file \"" + file.getName() + "\" can not be read.\n" + "Please select another file.", "File Not Readable", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                JFilePanel.this.setSelectedFile(file);
                for (ActionListener l : JFilePanel.this.listeners) {
                    l.actionPerformed(evt);
                }
            }
