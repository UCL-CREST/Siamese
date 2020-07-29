    public JPanel editeToi() {
        JPanel lePanneau = new JPanel(new GridLayout(0, 1));
        creerRadio(lePanneau);
        JPanel lePanneauInter = new JPanel();
        sonChampCarte = new JTextField(10);
        if (sonFichier != null) sonChampCarte.setText(sonFichier.getName()); else sonChampCarte.setText("carte m�re");
        sonChampCarte.setEditable(false);
        JButton leBouton = new JButton("Charger la carte");
        leBouton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent telleAction) {
                int leRetour = sonSelectionneurDeFichier.showOpenDialog(saCase.sonEditeurDonjon);
                if (leRetour == JFileChooser.APPROVE_OPTION) {
                    sonFichier = sonSelectionneurDeFichier.getSelectedFile();
                    sonChampCarte.setText(sonFichier.getName());
                }
            }
        });
        lePanneauInter.add(sonChampCarte);
        lePanneauInter.add(leBouton);
        lePanneau.add(lePanneauInter);
        JLabel leLabel = new JLabel("Laissez \"carte m�re\" si cette carte est la suivante d'une autre.");
        lePanneau.add(leLabel);
        lePanneauInter = new JPanel();
        leBouton = new JButton("Valider");
        leBouton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent telleAction) {
                if (sonFichier != null && sonFichier.exists()) {
                    saCarte = new UneCarte(sonFichier, saCase.sonEditeurDonjon);
                    sonNom = "Passage vers " + saCarte.getsonNom();
                } else {
                    saCarte = null;
                    sonNom = "Passage vers une carte m�re.";
                }
                JOptionPane.showMessageDialog(null, "Changement effectu�!");
            }
        });
        lePanneauInter.add(leBouton);
        lePanneau.add(lePanneauInter);
        return lePanneau;
    }
