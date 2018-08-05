            public void actionPerformed(ActionEvent telleAction) {
                int leRetour = sonSelectionneurDeFichier.showOpenDialog(saCase.sonEditeurDonjon);
                if (leRetour == JFileChooser.APPROVE_OPTION) {
                    sonFichier = sonSelectionneurDeFichier.getSelectedFile();
                    sonChampCarte.setText(sonFichier.getName());
                }
            }
