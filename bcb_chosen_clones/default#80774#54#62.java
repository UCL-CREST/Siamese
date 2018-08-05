            public void actionPerformed(ActionEvent e) {
                if (filechooser == null) {
                    filechooser = new JFileChooser();
                    filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                }
                if (filechooser.showOpenDialog(ImageDisplay.this) == JFileChooser.APPROVE_OPTION) {
                    open(filechooser.getSelectedFile());
                }
            }
