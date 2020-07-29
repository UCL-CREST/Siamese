            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileChooser.showOpenDialog(getMainWindow()) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    resetOutput();
                    try {
                        model.add(file.getName(), calculator.load(file));
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(getMainWindow(), "Unable to load matrix from file: " + file.getName());
                    }
                }
            }
