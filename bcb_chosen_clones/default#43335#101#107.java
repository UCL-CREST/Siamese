            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = createFileChooser();
                int result = fc.showOpenDialog(getDemoPanel());
                if (result == JFileChooser.APPROVE_OPTION) {
                    loadImage(fc.getSelectedFile().getPath());
                }
            }
