            public void actionPerformed(ActionEvent e) {
                JFileChooser choix = new JFileChooser();
                choix.addChoosableFileFilter(parent.new FilterImage());
                choix.setCurrentDirectory(new java.io.File(parent.NomCarte + "/Chipset"));
                int retour = choix.showOpenDialog(null);
                if (retour == JFileChooser.APPROVE_OPTION) {
                    if (!new File(parent.NomCarte + "/Chipset/" + choix.getSelectedFile().getName()).exists()) parent.copyfile(choix.getSelectedFile().getAbsolutePath(), parent.NomCarte + "/Chipset/" + choix.getSelectedFile().getName());
                    Ed_Chipset.setText("Chipset\\" + choix.getSelectedFile().getName());
                    SaveMonstre();
                    LoadImage();
                }
            }
