            public void actionPerformed(ActionEvent ae) {
                String IDir = jtGraphPath.getText();
                if (IDir.trim().equals("")) IDir = "./";
                JFileChooser jfc = new JFileChooser(IDir);
                jfc.setDialogType(JFileChooser.DIRECTORIES_ONLY);
                jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                if (jfc.showOpenDialog(dlgNew) == JFileChooser.APPROVE_OPTION) {
                    jtGraphPath.setText(jfc.getSelectedFile().getPath());
                }
            }
