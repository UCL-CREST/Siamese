    public OngletMagies(FenetreSimple p) {
        setLayout(null);
        parent = p;
        Font font = new Font("MS Sans Serif", Font.PLAIN, 10);
        Magiemodel = new DefaultListModel();
        MagieList = new JList(Magiemodel);
        MagieList.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                if (MagieList.getSelectedIndex() >= 0) {
                    paneunemagie.setVisible(true);
                    AllowSave = false;
                    CB_Classe.removeAllItems();
                    CB_Classe.addItem("Aucune");
                    for (int i = 0; i < parent.general.getClassesJoueur().size(); i++) CB_Classe.addItem(parent.general.getClassesJoueur().get(i).Name);
                    Ed_Nom.setText(parent.general.getMagieByIndex(MagieList.getSelectedIndex()).Name);
                    Ed_Explication.setText(parent.general.getMagieByIndex(MagieList.getSelectedIndex()).Explication);
                    Ed_Chipset.setText(parent.general.getMagieByIndex(MagieList.getSelectedIndex()).Chipset);
                    Ed_X.setText(Integer.toString(parent.general.getMagieByIndex(MagieList.getSelectedIndex()).X));
                    Ed_Y.setText(Integer.toString(parent.general.getMagieByIndex(MagieList.getSelectedIndex()).Y));
                    Ed_W.setText(Integer.toString(parent.general.getMagieByIndex(MagieList.getSelectedIndex()).W));
                    Ed_H.setText(Integer.toString(parent.general.getMagieByIndex(MagieList.getSelectedIndex()).H));
                    Ed_Tran.setText(Integer.toString(parent.general.getMagieByIndex(MagieList.getSelectedIndex()).Tran));
                    Ed_SonMagie.setText(parent.general.getMagieByIndex(MagieList.getSelectedIndex()).SoundMagie);
                    Ed_FormuleZone.setText(parent.general.getMagieByIndex(MagieList.getSelectedIndex()).FormuleZone);
                    Ed_FormuleDuree.setText(parent.general.getMagieByIndex(MagieList.getSelectedIndex()).FormuleDuree);
                    Ed_FormuleTouche.setText(parent.general.getMagieByIndex(MagieList.getSelectedIndex()).FormuleTouche);
                    Ed_FormuleEffet.setText(parent.general.getMagieByIndex(MagieList.getSelectedIndex()).FormuleEffet);
                    Ed_MPNeeded.setText(Integer.toString(parent.general.getMagieByIndex(MagieList.getSelectedIndex()).MPNeeded));
                    Ed_LvlMin.setText(Integer.toString(parent.general.getMagieByIndex(MagieList.getSelectedIndex()).LvlMin));
                    Ed_TempsIncantation.setText(Integer.toString(parent.general.getMagieByIndex(MagieList.getSelectedIndex()).TempsIncantation));
                    Ed_DureeAnim.setText(Integer.toString(parent.general.getMagieByIndex(MagieList.getSelectedIndex()).DureeAnim));
                    CB_Classe.setSelectedIndex(parent.general.getMagieByIndex(MagieList.getSelectedIndex()).Classe);
                    switch(parent.general.getMagieByIndex(MagieList.getSelectedIndex()).MagieType) {
                        case 0:
                            RB_SortCibleUnique.setSelected(true);
                            break;
                        case 1:
                            RB_Bouledefeu.setSelected(true);
                            break;
                        case 2:
                            RB_EffetZone.setSelected(true);
                            break;
                        case 3:
                            RB_Teleportation.setSelected(true);
                            break;
                        case 4:
                            RB_Resurrection.setSelected(true);
                            break;
                        case 5:
                            RB_SautAttaque.setSelected(true);
                            break;
                        case 6:
                            RB_AttaqueSournoise.setSelected(true);
                            break;
                    }
                    switch(parent.general.getMagieByIndex(MagieList.getSelectedIndex()).OnMonster) {
                        case 0:
                            RB_CibleMonstre.setSelected(true);
                            break;
                        case 1:
                            RB_CibleJoueur.setSelected(true);
                            break;
                        case 2:
                            RB_AutourJoueur.setSelected(true);
                            break;
                    }
                    switch(parent.general.getMagieByIndex(MagieList.getSelectedIndex()).Z) {
                        case 0:
                            RB_PosDynamique.setSelected(true);
                            break;
                        case 1:
                            RB_PosAuDessus.setSelected(true);
                            break;
                    }
                    LoadImage();
                    AllowSave = true;
                }
            }
        });
        scrollpanemaglist = new JScrollPane(MagieList);
        scrollpanemaglist.setBounds(new Rectangle(10, 10, 190, 470));
        add(scrollpanemaglist);
        JButton Bt_AjouteMagie = new JButton("Ajouter une magie");
        Bt_AjouteMagie.setBounds(new Rectangle(205, 10, 180, 20));
        add(Bt_AjouteMagie);
        Bt_AjouteMagie.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String NomMagie = JOptionPane.showInputDialog(null, "Entrez le nom de la nouvelle magie", "", 1);
                if (NomMagie != null) {
                    if (NomMagie.compareTo("") != 0) {
                        Magiemodel.add(MagieList.getModel().getSize(), NomMagie);
                        parent.general.getMagies().add(parent.general.new Magie(NomMagie));
                        parent.monstres.StatsBaseChange();
                    }
                }
            }
        });
        JButton Bt_RetireMagie = new JButton("Retirer une magie");
        Bt_RetireMagie.setBounds(new Rectangle(400, 10, 180, 20));
        Bt_RetireMagie.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (MagieList.getSelectedIndex() >= 0) {
                    if (JOptionPane.showConfirmDialog(null, "Etes vous sûr de vouloir effacer cette magie?", "Effacer", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                        paneunemagie.setVisible(false);
                        for (int i = 0; i < parent.general.getMonstres().size(); i++) {
                            for (int j = 0; j < parent.general.getMonstreByIndex(i).Spell.size(); j++) {
                                if (parent.general.getMonstreByIndex(i).Spell.get(j) > MagieList.getSelectedIndex()) parent.general.getMonstreByIndex(i).ObjectWin.set(j, (short) (parent.general.getMonstreByIndex(i).ObjectWin.get(j) - 1));
                            }
                        }
                        parent.general.getMagies().remove(MagieList.getSelectedIndex());
                        Magiemodel.remove(MagieList.getSelectedIndex());
                        parent.monstres.StatsBaseChange();
                    }
                }
            }
        });
        add(Bt_RetireMagie);
        paneunemagie = new JPanel();
        paneunemagie.setLayout(null);
        paneunemagie.setBounds(new Rectangle(205, 35, 580, 550));
        paneunemagie.setVisible(false);
        add(paneunemagie);
        paneimg = new JLabel();
        paneimg.setBorder(BorderFactory.createLoweredBevelBorder());
        paneimg.setLayout(null);
        paneimg.setBounds(new Rectangle(0, 0, 112, 80));
        paneimg.setVerticalAlignment(JLabel.TOP);
        paneimg.setHorizontalAlignment(JLabel.LEFT);
        paneunemagie.add(paneimg);
        JLabel NomMagie = new JLabel("Nom : ");
        NomMagie.setBounds(new Rectangle(120, 0, 200, 20));
        paneunemagie.add(NomMagie);
        JLabel Explication = new JLabel("Explication : ");
        Explication.setBounds(new Rectangle(120, 20, 200, 20));
        paneunemagie.add(Explication);
        JLabel Classe = new JLabel("Classe : ");
        Classe.setBounds(new Rectangle(120, 40, 200, 20));
        paneunemagie.add(Classe);
        JLabel Chipset = new JLabel("Chipset : ");
        Chipset.setBounds(new Rectangle(120, 60, 200, 20));
        paneunemagie.add(Chipset);
        JLabel FormuleZone = new JLabel("Zone d'effet : ");
        FormuleZone.setBounds(new Rectangle(120, 100, 200, 20));
        paneunemagie.add(FormuleZone);
        JLabel FormuleDuree = new JLabel("Durée : ");
        FormuleDuree.setBounds(new Rectangle(120, 120, 200, 20));
        paneunemagie.add(FormuleDuree);
        JLabel FormuleTouche = new JLabel("Toucher : ");
        FormuleTouche.setBounds(new Rectangle(120, 140, 200, 20));
        paneunemagie.add(FormuleTouche);
        JLabel FormuleEffet = new JLabel("Effet : ");
        FormuleEffet.setBounds(new Rectangle(120, 160, 200, 20));
        paneunemagie.add(FormuleEffet);
        KeyListener keyListener = new KeyListener() {

            public void keyTyped(KeyEvent keyEvent) {
            }

            public void keyPressed(KeyEvent keyEvent) {
            }

            public void keyReleased(KeyEvent keyEvent) {
                if ((keyEvent.getSource().equals(Ed_X)) || (keyEvent.getSource().equals(Ed_Y)) || (keyEvent.getSource().equals(Ed_W)) || (keyEvent.getSource().equals(Ed_H))) RefreshImage();
                SaveMagie();
            }
        };
        Ed_Nom = new JTextField();
        Ed_Nom.setBounds(new Rectangle(210, 0, 340, 20));
        Ed_Nom.addKeyListener(new KeyListener() {

            public void keyTyped(KeyEvent keyEvent) {
            }

            public void keyPressed(KeyEvent keyEvent) {
            }

            public void keyReleased(KeyEvent e) {
                parent.general.getMagieByIndex(MagieList.getSelectedIndex()).Name = Ed_Nom.getText();
                Magiemodel.set(MagieList.getSelectedIndex(), Ed_Nom.getText());
            }
        });
        paneunemagie.add(Ed_Nom);
        Ed_Explication = new JTextField();
        Ed_Explication.setBounds(new Rectangle(210, 20, 340, 20));
        Ed_Explication.addKeyListener(keyListener);
        paneunemagie.add(Ed_Explication);
        CB_Classe = new JComboBox();
        CB_Classe.setBounds(new Rectangle(210, 40, 260, 20));
        ActionListener ac = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                SaveMagie();
            }
        };
        CB_Classe.addActionListener(ac);
        paneunemagie.add(CB_Classe);
        Ed_Chipset = new JTextField();
        Ed_Chipset.setBounds(new Rectangle(210, 60, 260, 20));
        Ed_Chipset.addKeyListener(keyListener);
        paneunemagie.add(Ed_Chipset);
        JButton Bt_ChooseChipset = new JButton("...");
        Bt_ChooseChipset.setBounds(new Rectangle(471, 60, 18, 20));
        Bt_ChooseChipset.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JFileChooser choix = new JFileChooser();
                choix.addChoosableFileFilter(parent.new FilterImage());
                choix.setCurrentDirectory(new java.io.File(parent.NomCarte + "/Chipset"));
                int retour = choix.showOpenDialog(null);
                if (retour == JFileChooser.APPROVE_OPTION) {
                    if (!new File(parent.NomCarte + "/Chipset/" + choix.getSelectedFile().getName()).exists()) parent.copyfile(choix.getSelectedFile().getAbsolutePath(), parent.NomCarte + "/Chipset/" + choix.getSelectedFile().getName());
                    Ed_Chipset.setText("Chipset\\" + choix.getSelectedFile().getName());
                    SaveMagie();
                    LoadImage();
                }
            }
        });
        paneunemagie.add(Bt_ChooseChipset);
        JLabel Lbl_X = new JLabel("X :");
        Lbl_X.setFont(font);
        Lbl_X.setBounds(new Rectangle(0, 90, 30, 17));
        paneunemagie.add(Lbl_X);
        Ed_X = new JTextField();
        Ed_X.setFont(font);
        Ed_X.setBounds(new Rectangle(20, 90, 33, 17));
        Ed_X.addKeyListener(keyListener);
        paneunemagie.add(Ed_X);
        JLabel Lbl_Y = new JLabel("Y :");
        Lbl_Y.setFont(font);
        Lbl_Y.setBounds(new Rectangle(60, 90, 30, 17));
        paneunemagie.add(Lbl_Y);
        Ed_Y = new JTextField();
        Ed_Y.setFont(font);
        Ed_Y.setBounds(new Rectangle(80, 90, 33, 17));
        Ed_Y.addKeyListener(keyListener);
        paneunemagie.add(Ed_Y);
        JLabel Lbl_W = new JLabel("W :");
        Lbl_W.setFont(font);
        Lbl_W.setBounds(new Rectangle(0, 112, 30, 17));
        paneunemagie.add(Lbl_W);
        Ed_W = new JTextField();
        Ed_W.setFont(font);
        Ed_W.setBounds(new Rectangle(20, 112, 33, 17));
        Ed_W.addKeyListener(keyListener);
        paneunemagie.add(Ed_W);
        JLabel Lbl_H = new JLabel("H :");
        Lbl_H.setFont(font);
        Lbl_H.setBounds(new Rectangle(60, 112, 30, 17));
        paneunemagie.add(Lbl_H);
        Ed_H = new JTextField();
        Ed_H.setFont(font);
        Ed_H.setBounds(new Rectangle(80, 112, 33, 17));
        Ed_H.addKeyListener(keyListener);
        paneunemagie.add(Ed_H);
        JLabel Lbl_Tran = new JLabel("Transparence :");
        Lbl_Tran.setFont(font);
        Lbl_Tran.setBounds(new Rectangle(0, 134, 100, 17));
        paneunemagie.add(Lbl_Tran);
        Ed_Tran = new JTextField();
        Ed_Tran.setFont(font);
        Ed_Tran.setBounds(new Rectangle(80, 134, 33, 17));
        Ed_Tran.addKeyListener(keyListener);
        paneunemagie.add(Ed_Tran);
        JLabel SoundMagie = new JLabel("Son : ");
        SoundMagie.setBounds(new Rectangle(120, 80, 200, 20));
        paneunemagie.add(SoundMagie);
        Ed_SonMagie = new JTextField();
        Ed_SonMagie.setBounds(new Rectangle(210, 80, 260, 20));
        Ed_SonMagie.addKeyListener(keyListener);
        paneunemagie.add(Ed_SonMagie);
        ActionListener ChooseSound = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JFileChooser choix = new JFileChooser();
                choix.addChoosableFileFilter(parent.new FilterSound());
                choix.setCurrentDirectory(new java.io.File(parent.NomCarte + "/Sound"));
                int retour = choix.showOpenDialog(null);
                if (retour == JFileChooser.APPROVE_OPTION) {
                    if (!new File(parent.NomCarte + "/Sound/" + choix.getSelectedFile().getName()).exists()) parent.copyfile(choix.getSelectedFile().getAbsolutePath(), parent.NomCarte + "/Sound/" + choix.getSelectedFile().getName());
                    JTextField Edit = null;
                    if (e.getSource().equals(Bt_ChooseSonMagie)) Edit = Ed_SonMagie;
                    if (Edit != null) Edit.setText("Sound\\" + choix.getSelectedFile().getName());
                    SaveMagie();
                }
            }
        };
        ActionListener stwizard = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                StatWizard sw;
                String s = "", commentaire = "";
                String[] varspecial = null, combospecial = null;
                if (e.getSource() == Bt_FormuleZone) {
                    s = Ed_FormuleZone.getText();
                    commentaire = "Zone d'effet du sort. Le sort sera aggrandi X fois le chiffre résultant de la formule";
                }
                if (e.getSource() == Bt_FormuleDuree) {
                    s = Ed_FormuleDuree.getText();
                    commentaire = "Durée du sort. Le sort durera X fois le chiffre résultant de la formule";
                }
                if (e.getSource() == Bt_FormuleTouche) {
                    s = Ed_FormuleTouche.getText();
                    commentaire = "Formule qui détermine si le sort touche sa cible ou non. Un chiffre > 0 touche, sinon le sort rate sa cible";
                }
                if (e.getSource() == Bt_FormuleEffet) {
                    s = Ed_FormuleEffet.getText();
                    commentaire = "Effet du sort. Ex : %Cible.Stat%=Formule; ou spell:Sort; Le sort peut avoir plusieurs effets, séparez les effets par des points virgules.";
                    varspecial = new String[1];
                    varspecial[0] = "spell:";
                    combospecial = new String[parent.general.getMagies().size() + 1];
                    combospecial[0] = "Magie :";
                    for (int j = 0; j < parent.general.getMagies().size(); j++) combospecial[j + 1] = parent.general.getMagieByIndex(j).Name;
                }
                int compte;
                compte = 16 + parent.general.getStatsBase().size();
                String[] values = new String[compte];
                for (int i = 0; i < parent.general.getStatsBase().size(); i++) values[i] = "%Wizard." + parent.general.getStatsBase().get(i) + "%";
                compte = parent.general.getStatsBase().size();
                values[compte] = "%Wizard.Attaque%";
                values[compte + 1] = "%Wizard.Esquive%";
                values[compte + 2] = "%Wizard.Vie%";
                values[compte + 3] = "%Wizard.VieMax%";
                values[compte + 4] = "%Wizard.CurrentMag%";
                values[compte + 5] = "%Wizard.MagMax%";
                values[compte + 6] = "%Wizard.Gold%";
                values[compte + 7] = "%Wizard.Lvl%";
                values[compte + 8] = "%Wizard.LvlPoint%";
                values[compte + 9] = "%Wizard.CurrentXP%";
                values[compte + 10] = "%Wizard.NextXP%";
                values[compte + 11] = "%rand(100)%";
                values[compte + 12] = "%max(valeur1,valeur2)%";
                values[compte + 13] = "%min(valeur1,valeur2)%";
                values[compte + 14] = "Variable[nomvar]";
                values[compte + 15] = "spell:";
                String[] cible = new String[] { "%Cible.Attaque%", "%Cible.Esquive%", "%Cible.Vie%", "%Cible.Lvl%", "%Cible.VieMax%", "%Cible.Degat%", "%Cible.Defense%", "%Cible.Bloque%" };
                sw = new StatWizard(parent.general, s, "Variables relatives au magicien : ", "Variables relatives à la cible : ", commentaire, values, cible, combospecial, null, true);
                if (sw.status == 1) {
                    if (e.getSource() == Bt_FormuleZone) Ed_FormuleZone.setText(sw.Ed_Commande.getText());
                    if (e.getSource() == Bt_FormuleDuree) Ed_FormuleDuree.setText(sw.Ed_Commande.getText());
                    if (e.getSource() == Bt_FormuleTouche) Ed_FormuleTouche.setText(sw.Ed_Commande.getText());
                    if (e.getSource() == Bt_FormuleEffet) Ed_FormuleEffet.setText(sw.Ed_Commande.getText());
                }
                SaveMagie();
                sw.dispose();
            }
        };
        Bt_ChooseSonMagie = new JButton("...");
        Bt_ChooseSonMagie.setBounds(new Rectangle(471, 80, 18, 20));
        Bt_ChooseSonMagie.addActionListener(ChooseSound);
        paneunemagie.add(Bt_ChooseSonMagie);
        Ed_FormuleZone = new JTextField();
        Ed_FormuleZone.setBounds(new Rectangle(210, 100, 320, 20));
        Ed_FormuleZone.addKeyListener(keyListener);
        paneunemagie.add(Ed_FormuleZone);
        Bt_FormuleZone = new JButton("...");
        Bt_FormuleZone.setBounds(new Rectangle(531, 100, 18, 20));
        Bt_FormuleZone.addActionListener(stwizard);
        paneunemagie.add(Bt_FormuleZone);
        Ed_FormuleDuree = new JTextField();
        Ed_FormuleDuree.setBounds(new Rectangle(210, 120, 320, 20));
        Ed_FormuleDuree.addKeyListener(keyListener);
        paneunemagie.add(Ed_FormuleDuree);
        Bt_FormuleDuree = new JButton("...");
        Bt_FormuleDuree.setBounds(new Rectangle(531, 120, 18, 20));
        Bt_FormuleDuree.addActionListener(stwizard);
        paneunemagie.add(Bt_FormuleDuree);
        Ed_FormuleTouche = new JTextField();
        Ed_FormuleTouche.setBounds(new Rectangle(210, 140, 320, 20));
        Ed_FormuleTouche.addKeyListener(keyListener);
        paneunemagie.add(Ed_FormuleTouche);
        Bt_FormuleTouche = new JButton("...");
        Bt_FormuleTouche.setBounds(new Rectangle(531, 140, 18, 20));
        Bt_FormuleTouche.addActionListener(stwizard);
        paneunemagie.add(Bt_FormuleTouche);
        Ed_FormuleEffet = new JTextField();
        Ed_FormuleEffet.setBounds(new Rectangle(210, 160, 320, 20));
        Ed_FormuleEffet.addKeyListener(keyListener);
        paneunemagie.add(Ed_FormuleEffet);
        Bt_FormuleEffet = new JButton("...");
        Bt_FormuleEffet.setBounds(new Rectangle(531, 160, 18, 20));
        Bt_FormuleEffet.addActionListener(stwizard);
        paneunemagie.add(Bt_FormuleEffet);
        JLabel Lbl_Cout = new JLabel("Coût MP : ");
        Lbl_Cout.setBounds(new Rectangle(120, 180, 200, 20));
        paneunemagie.add(Lbl_Cout);
        Ed_MPNeeded = new JTextField();
        Ed_MPNeeded.setBounds(new Rectangle(260, 180, 90, 20));
        Ed_MPNeeded.addKeyListener(keyListener);
        paneunemagie.add(Ed_MPNeeded);
        JLabel Lbl_LvlMin = new JLabel("Level min : ");
        Lbl_LvlMin.setBounds(new Rectangle(365, 180, 200, 20));
        paneunemagie.add(Lbl_LvlMin);
        Ed_LvlMin = new JTextField();
        Ed_LvlMin.setBounds(new Rectangle(460, 180, 90, 20));
        Ed_LvlMin.addKeyListener(keyListener);
        paneunemagie.add(Ed_LvlMin);
        JLabel Lbl_DureeIncant = new JLabel("Durée incantation : ");
        Lbl_DureeIncant.setBounds(new Rectangle(120, 200, 200, 20));
        paneunemagie.add(Lbl_DureeIncant);
        Ed_TempsIncantation = new JTextField();
        Ed_TempsIncantation.setBounds(new Rectangle(260, 200, 90, 20));
        Ed_TempsIncantation.addKeyListener(keyListener);
        paneunemagie.add(Ed_TempsIncantation);
        JLabel Lbl_DureeAnim = new JLabel("Durée anim : ");
        Lbl_DureeAnim.setBounds(new Rectangle(365, 200, 200, 20));
        paneunemagie.add(Lbl_DureeAnim);
        Ed_DureeAnim = new JTextField();
        Ed_DureeAnim.setBounds(new Rectangle(460, 200, 90, 20));
        Ed_DureeAnim.addKeyListener(keyListener);
        paneunemagie.add(Ed_DureeAnim);
        JPanel panetypemag = new JPanel();
        TitledBorder title = BorderFactory.createTitledBorder("Type de magie");
        title.setTitleFont(font);
        panetypemag.setBorder(title);
        panetypemag.setBounds(new Rectangle(0, 220, 170, 160));
        panetypemag.setLayout(new GridLayout(0, 1));
        paneunemagie.add(panetypemag);
        ButtonGroup group = new ButtonGroup();
        RB_SortCibleUnique = new JRadioButton("A cible unique");
        RB_SortCibleUnique.addActionListener(ac);
        group.add(RB_SortCibleUnique);
        panetypemag.add(RB_SortCibleUnique);
        RB_Bouledefeu = new JRadioButton("Boule de feu");
        RB_Bouledefeu.addActionListener(ac);
        group.add(RB_Bouledefeu);
        panetypemag.add(RB_Bouledefeu);
        RB_EffetZone = new JRadioButton("Effet de zone");
        RB_EffetZone.addActionListener(ac);
        group.add(RB_EffetZone);
        panetypemag.add(RB_EffetZone);
        RB_Teleportation = new JRadioButton("Téléportation");
        RB_Teleportation.addActionListener(ac);
        RB_Teleportation.setEnabled(false);
        group.add(RB_Teleportation);
        panetypemag.add(RB_Teleportation);
        RB_Resurrection = new JRadioButton("Résurrection");
        RB_Resurrection.addActionListener(ac);
        RB_Resurrection.setEnabled(false);
        group.add(RB_Resurrection);
        panetypemag.add(RB_Resurrection);
        RB_SautAttaque = new JRadioButton("Saut d'attaque");
        RB_SautAttaque.addActionListener(ac);
        group.add(RB_SautAttaque);
        panetypemag.add(RB_SautAttaque);
        RB_AttaqueSournoise = new JRadioButton("Attaque sournoise");
        RB_AttaqueSournoise.addActionListener(ac);
        group.add(RB_AttaqueSournoise);
        panetypemag.add(RB_AttaqueSournoise);
        JPanel panez = new JPanel();
        title = BorderFactory.createTitledBorder("Position");
        title.setTitleFont(font);
        panez.setBorder(title);
        panez.setBounds(new Rectangle(200, 220, 170, 60));
        panez.setLayout(new GridLayout(0, 1));
        paneunemagie.add(panez);
        group = new ButtonGroup();
        RB_PosDynamique = new JRadioButton("Dynamique");
        RB_PosDynamique.addActionListener(ac);
        group.add(RB_PosDynamique);
        panez.add(RB_PosDynamique);
        RB_PosAuDessus = new JRadioButton("Au dessus");
        RB_PosAuDessus.addActionListener(ac);
        group.add(RB_PosAuDessus);
        panez.add(RB_PosAuDessus);
        JPanel paneonmonster = new JPanel();
        title = BorderFactory.createTitledBorder("Cible par défaut");
        title.setTitleFont(font);
        paneonmonster.setBorder(title);
        paneonmonster.setBounds(new Rectangle(200, 280, 300, 100));
        paneonmonster.setLayout(new GridLayout(0, 1));
        paneunemagie.add(paneonmonster);
        group = new ButtonGroup();
        RB_CibleMonstre = new JRadioButton("Cible monstre");
        RB_CibleMonstre.addActionListener(ac);
        group.add(RB_CibleMonstre);
        paneonmonster.add(RB_CibleMonstre);
        RB_CibleJoueur = new JRadioButton("Cible joueur");
        RB_CibleJoueur.addActionListener(ac);
        group.add(RB_CibleJoueur);
        paneonmonster.add(RB_CibleJoueur);
        RB_AutourJoueur = new JRadioButton("Cible autour du joueur");
        RB_AutourJoueur.addActionListener(ac);
        group.add(RB_AutourJoueur);
        paneonmonster.add(RB_AutourJoueur);
        ComponentAdapter listener = new ComponentAdapter() {

            public void componentResized(ComponentEvent evt) {
                Component c = (Component) evt.getSource();
                Dimension newSize = c.getSize();
                scrollpanemaglist.setBounds(new Rectangle(10, 10, 190, newSize.height - 105));
            }
        };
        parent.addComponentListener(listener);
    }
