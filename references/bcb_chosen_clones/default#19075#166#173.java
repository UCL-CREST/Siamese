            public void actionPerformed(ActionEvent event) {
                int result = XMLFilechooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    IncludeInFilename = XMLFilechooser.getSelectedFile().getPath();
                    IncludeInFilenamelist = IncludeInFilenamelist + IncludeInFilename + ";";
                    IncludeFileNameField.setText(IncludeInFilenamelist);
                }
            }
