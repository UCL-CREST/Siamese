            public void actionPerformed(ActionEvent e) {
                if (fc == null) {
                    fc = new JFileChooser();
                    fc.addChoosableFileFilter(new ImageFilter());
                    fc.setAcceptAllFileFilterUsed(false);
                    fc.setFileView(new ImageFileView());
                    fc.setAccessory(new ImagePreview(fc));
                }
                int returnVal = fc.showOpenDialog(albumPanel);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    avatar.setImage(file);
                }
                fc.setSelectedFile(null);
            }
