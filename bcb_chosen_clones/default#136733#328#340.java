            public void actionPerformed(ActionEvent e) {
                JFileChooser choix = new JFileChooser();
                choix.addChoosableFileFilter(parent.new FilterSound());
                choix.setCurrentDirectory(new java.io.File(parent.NomCarte + "/Sound"));
                int retour = choix.showOpenDialog(null);
                if (retour == JFileChooser.APPROVE_OPTION) {
                    if (!new File(parent.NomCarte + "/Sound/" + choix.getSelectedFile().getName()).exists()) parent.copyfile(choix.getSelectedFile().getAbsolutePath(), parent.NomCarte + "/Sound/" + choix.getSelectedFile().getName());
                    JTextField Edit = null;
                    if (e.getSource().equals(Bt_ChooseSonAttaque)) Edit = Ed_SonAttaque; else if (e.getSource().equals(Bt_ChooseSonBlesse)) Edit = Ed_SonBlesse; else if (e.getSource().equals(Bt_ChooseSonMagie)) Edit = Ed_SonMagie;
                    if (Edit != null) Edit.setText("Sound\\" + choix.getSelectedFile().getName());
                    SaveMonstre();
                }
            }
