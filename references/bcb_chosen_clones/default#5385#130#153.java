            public void actionPerformed(ActionEvent event) {
                if (fileType.equals("XML")) {
                    int result = XMLFilechooser.showOpenDialog(null);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        InFilename = XMLFilechooser.getSelectedFile().getPath();
                        fileNameField.setText(InFilename);
                        actionButton.setEnabled(true);
                    }
                } else if (fileType.equals("ZIP (Content Packages and Packaged Tests)")) {
                    int result = ZIPFilechooser.showOpenDialog(null);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        InFilename = ZIPFilechooser.getSelectedFile().getPath();
                        fileNameField.setText(InFilename);
                        actionButton.setEnabled(true);
                    }
                } else if (fileType.equals("Directory")) {
                    int result = Directorychooser.showOpenDialog(null);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        InFilename = Directorychooser.getSelectedFile().getPath();
                        fileNameField.setText(InFilename);
                        actionButton.setEnabled(true);
                    }
                }
            }
