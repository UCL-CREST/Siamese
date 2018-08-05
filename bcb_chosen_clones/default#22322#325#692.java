        public ClassesJoueur(FenetreSimple p) {
            setLayout(null);
            parent = p;
            Classemodel = new DefaultListModel();
            ClasseList = new JList(Classemodel);
            ClasseList.addMouseListener(new MouseAdapter() {

                public void mousePressed(MouseEvent e) {
                    if (ClasseList.getSelectedIndex() >= 0) {
                        paneuneclasse.setVisible(true);
                        Ed_Nom.setText(parent.general.getClassesJoueur().get(ClasseList.getSelectedIndex()).Name);
                        Ed_FormuleAtt.setText(parent.general.getClassesJoueur().get(ClasseList.getSelectedIndex()).FormuleAttaque);
                        Ed_FormuleEsq.setText(parent.general.getClassesJoueur().get(ClasseList.getSelectedIndex()).FormuleEsquive);
                        Ed_FormuleDeg.setText(parent.general.getClassesJoueur().get(ClasseList.getSelectedIndex()).FormuleDegat);
                        Ed_FormuleDef.setText(parent.general.getClassesJoueur().get(ClasseList.getSelectedIndex()).FormuleDefense);
                        Ed_FormuleXP.setText(parent.general.getClassesJoueur().get(ClasseList.getSelectedIndex()).FormuleXP);
                        Ed_FormuleGold.setText(parent.general.getClassesJoueur().get(ClasseList.getSelectedIndex()).FormuleGold);
                        Ed_FormuleVieMax.setText(parent.general.getClassesJoueur().get(ClasseList.getSelectedIndex()).FormuleVieMax);
                        Ed_FormuleMagMax.setText(parent.general.getClassesJoueur().get(ClasseList.getSelectedIndex()).FormuleMagMax);
                        Ed_SonAttaque.setText(parent.general.getClassesJoueur().get(ClasseList.getSelectedIndex()).SoundAttaque);
                        Ed_SonBlesse.setText(parent.general.getClassesJoueur().get(ClasseList.getSelectedIndex()).SoundWound);
                        Ed_SonMagie.setText(parent.general.getClassesJoueur().get(ClasseList.getSelectedIndex()).SoundConcentration);
                        Ed_LvlUpPoint.setText(Integer.toString(parent.general.getClassesJoueur().get(ClasseList.getSelectedIndex()).LvlupPoint));
                        Ed_LvlMax.setText(Integer.toString(parent.general.getClassesJoueur().get(ClasseList.getSelectedIndex()).LvlMax));
                        StdGridModel std = new StdGridModel("Stats Min", parent.general.getClassesJoueur().get(ClasseList.getSelectedIndex()).StatsMin);
                        StatsMinGrid.setModel(std);
                        StatsMinGrid.updateUI();
                        std = new StdGridModel("Stats Max", parent.general.getClassesJoueur().get(ClasseList.getSelectedIndex()).StatsMax);
                        StatsMaxGrid.setModel(std);
                        StatsMaxGrid.updateUI();
                    }
                }
            });
            scrollpaneclasslist = new JScrollPane(ClasseList);
            scrollpaneclasslist.setBounds(new Rectangle(10, 10, 190, 470));
            add(scrollpaneclasslist);
            JButton Bt_AjouteClasse = new JButton("Ajouter une classe");
            Bt_AjouteClasse.setBounds(new Rectangle(205, 10, 180, 20));
            add(Bt_AjouteClasse);
            Bt_AjouteClasse.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    String NomClasse = JOptionPane.showInputDialog(null, "Entrez le nom de la nouvelle classe", "", 1);
                    if (NomClasse != null) {
                        if (NomClasse.compareTo("") != 0) {
                            Classemodel.add(ClasseList.getModel().getSize(), NomClasse);
                            parent.general.getClassesJoueur().add(parent.general.new ClasseJoueur(NomClasse));
                            ArrayList<Integer> statsmin = parent.general.getClassesJoueur().get(parent.general.getClassesJoueur().size() - 1).StatsMin;
                            ArrayList<Integer> statsmax = parent.general.getClassesJoueur().get(parent.general.getClassesJoueur().size() - 1).StatsMax;
                            for (int i = 0; i < parent.general.getStatsBase().size(); i++) {
                                statsmin.add(0);
                                statsmax.add(0);
                            }
                            parent.objets.StatsBaseChange();
                            parent.magies.StatsBaseChange();
                        }
                    }
                }
            });
            JButton Bt_RetireClasse = new JButton("Retirer une classe");
            Bt_RetireClasse.setBounds(new Rectangle(400, 10, 180, 20));
            Bt_RetireClasse.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (ClasseList.getSelectedIndex() >= 0) {
                        if (JOptionPane.showConfirmDialog(null, "Etes vous sûr de vouloir effacer cette classe?", "Effacer", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                            parent.general.getClassesJoueur().remove(ClasseList.getSelectedIndex());
                            Classemodel.remove(ClasseList.getSelectedIndex());
                            paneuneclasse.setVisible(false);
                            parent.objets.StatsBaseChange();
                            parent.magies.StatsBaseChange();
                        }
                    }
                }
            });
            add(Bt_RetireClasse);
            paneuneclasse = new JPanel();
            paneuneclasse.setLayout(null);
            paneuneclasse.setBounds(new Rectangle(205, 35, 550, 550));
            paneuneclasse.setVisible(false);
            add(paneuneclasse);
            JLabel NomClasse = new JLabel("Nom : ");
            NomClasse.setBounds(new Rectangle(0, 0, 200, 20));
            paneuneclasse.add(NomClasse);
            JLabel FormuleAtt = new JLabel("Formule d'attaque : ");
            FormuleAtt.setBounds(new Rectangle(0, 20, 200, 20));
            paneuneclasse.add(FormuleAtt);
            JLabel FormuleEsq = new JLabel("Formule d'esquive : ");
            FormuleEsq.setBounds(new Rectangle(0, 40, 200, 20));
            paneuneclasse.add(FormuleEsq);
            JLabel FormuleDeg = new JLabel("Formule de dégat : ");
            FormuleDeg.setBounds(new Rectangle(0, 60, 200, 20));
            paneuneclasse.add(FormuleDeg);
            JLabel FormuleDef = new JLabel("Formule de défense : ");
            FormuleDef.setBounds(new Rectangle(0, 80, 200, 20));
            paneuneclasse.add(FormuleDef);
            JLabel FormuleXP = new JLabel("Formule gain XP : ");
            FormuleXP.setBounds(new Rectangle(0, 100, 200, 20));
            paneuneclasse.add(FormuleXP);
            JLabel FormuleGold = new JLabel("Formule gain or : ");
            FormuleGold.setBounds(new Rectangle(0, 120, 200, 20));
            paneuneclasse.add(FormuleGold);
            JLabel FormuleVieMax = new JLabel("Formule vie max : ");
            FormuleVieMax.setBounds(new Rectangle(0, 140, 200, 20));
            paneuneclasse.add(FormuleVieMax);
            JLabel FormuleMagMax = new JLabel("Formule magie max : ");
            FormuleMagMax.setBounds(new Rectangle(0, 160, 200, 20));
            paneuneclasse.add(FormuleMagMax);
            JLabel SoundAttaque = new JLabel("Son d'attaque : ");
            SoundAttaque.setBounds(new Rectangle(0, 180, 200, 20));
            paneuneclasse.add(SoundAttaque);
            JLabel LvlUpPoint = new JLabel("Point Lvl Up : ");
            LvlUpPoint.setBounds(new Rectangle(410, 180, 200, 20));
            paneuneclasse.add(LvlUpPoint);
            JLabel SoundWound = new JLabel("Son blessure : ");
            SoundWound.setBounds(new Rectangle(0, 200, 200, 20));
            paneuneclasse.add(SoundWound);
            JLabel LvlMax = new JLabel("Lvl Max : ");
            LvlMax.setBounds(new Rectangle(410, 200, 200, 20));
            paneuneclasse.add(LvlMax);
            JLabel SoundConcentration = new JLabel("Son magie : ");
            SoundConcentration.setBounds(new Rectangle(0, 220, 200, 20));
            paneuneclasse.add(SoundConcentration);
            KeyListener keyListener = new KeyListener() {

                public void keyTyped(KeyEvent keyEvent) {
                }

                public void keyPressed(KeyEvent keyEvent) {
                }

                public void keyReleased(KeyEvent keyEvent) {
                    SaveClasses();
                }
            };
            Ed_Nom = new JTextField();
            Ed_Nom.setBounds(new Rectangle(170, 0, 360, 20));
            Ed_Nom.addKeyListener(new KeyListener() {

                public void keyTyped(KeyEvent keyEvent) {
                }

                public void keyPressed(KeyEvent keyEvent) {
                }

                public void keyReleased(KeyEvent e) {
                    parent.general.getClassesJoueur().get(ClasseList.getSelectedIndex()).Name = Ed_Nom.getText();
                    Classemodel.set(ClasseList.getSelectedIndex(), Ed_Nom.getText());
                }
            });
            paneuneclasse.add(Ed_Nom);
            ActionListener stwizard = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    StatWizard sw;
                    String s = "";
                    if (e.getSource() == Bt_FormuleAtt) s = Ed_FormuleAtt.getText();
                    if (e.getSource() == Bt_FormuleEsq) s = Ed_FormuleEsq.getText();
                    if (e.getSource() == Bt_FormuleDeg) s = Ed_FormuleDeg.getText();
                    if (e.getSource() == Bt_FormuleDef) s = Ed_FormuleDef.getText();
                    if (e.getSource() == Bt_FormuleXP) s = Ed_FormuleXP.getText();
                    if (e.getSource() == Bt_FormuleGold) s = Ed_FormuleGold.getText();
                    if (e.getSource() == Bt_FormuleVieMax) s = Ed_FormuleVieMax.getText();
                    if (e.getSource() == Bt_FormuleMagMax) s = Ed_FormuleMagMax.getText();
                    int compte;
                    compte = 33 + parent.general.getStatsBase().size();
                    String[] values = new String[compte];
                    for (int i = 0; i < parent.general.getStatsBase().size(); i++) values[i] = "%" + parent.general.getStatsBase().get(i) + "%";
                    compte = parent.general.getStatsBase().size();
                    values[compte] = "%Vie%";
                    values[compte + 1] = "%VieMax%";
                    values[compte + 2] = "%CurrentMag%";
                    values[compte + 3] = "%MagMax%";
                    values[compte + 4] = "%Gold%";
                    values[compte + 5] = "%Lvl%";
                    values[compte + 6] = "%LvlPoint%";
                    values[compte + 7] = "%CurrentXP%";
                    values[compte + 8] = "%NextXP%";
                    values[compte + 9] = "%rand(100)%";
                    values[compte + 10] = "%max(valeur1,valeur2)%";
                    values[compte + 11] = "%min(valeur1,valeur2)%";
                    values[compte + 12] = "%Arme.Prix%";
                    values[compte + 13] = "%Arme.Attaque%";
                    values[compte + 14] = "%Arme.Defense%";
                    values[compte + 15] = "%Arme.Vie%";
                    values[compte + 16] = "%Arme.Magie%";
                    values[compte + 17] = "%Armure.Prix%";
                    values[compte + 18] = "%Armure.Attaque%";
                    values[compte + 19] = "%Armure.Defense%";
                    values[compte + 20] = "%Armure.Vie%";
                    values[compte + 21] = "%Armure.Magie%";
                    values[compte + 22] = "%Bouclier.Prix%";
                    values[compte + 23] = "%Bouclier.Attaque%";
                    values[compte + 24] = "%Bouclier.Defense%";
                    values[compte + 25] = "%Bouclier.Vie%";
                    values[compte + 26] = "%Bouclier.Magie%";
                    values[compte + 27] = "%Casque.Prix%";
                    values[compte + 28] = "%Casque.Attaque%";
                    values[compte + 29] = "%Casque.Defense%";
                    values[compte + 30] = "%Casque.Vie%";
                    values[compte + 31] = "%Casque.Magie%";
                    values[compte + 32] = "Variable[nomvar]";
                    String[] cible = new String[] { "%Monstre.Attaque%", "%Monstre.Esquive%", "%Monstre.Vie%", "%Monstre.Lvl%", "%Monstre.VieMax%", "%Monstre.Degat%", "%Monstre.Defense%", "%Monstre.XPMin%", "%Monstre.XPMax%", "%Monstre.GoldMin%", "%Monstre.GoldMax%" };
                    sw = new StatWizard(parent.general, s, "Variables relatives au joueur : ", "Variables relatives aux monstres : ", "", values, cible, null, null, true);
                    if (sw.status == 1) {
                        if (e.getSource() == Bt_FormuleAtt) Ed_FormuleAtt.setText(sw.Ed_Commande.getText());
                        if (e.getSource() == Bt_FormuleEsq) Ed_FormuleEsq.setText(sw.Ed_Commande.getText());
                        if (e.getSource() == Bt_FormuleDeg) Ed_FormuleDeg.setText(sw.Ed_Commande.getText());
                        if (e.getSource() == Bt_FormuleDef) Ed_FormuleDef.setText(sw.Ed_Commande.getText());
                        if (e.getSource() == Bt_FormuleXP) Ed_FormuleXP.setText(sw.Ed_Commande.getText());
                        if (e.getSource() == Bt_FormuleGold) Ed_FormuleGold.setText(sw.Ed_Commande.getText());
                        if (e.getSource() == Bt_FormuleVieMax) Ed_FormuleVieMax.setText(sw.Ed_Commande.getText());
                        if (e.getSource() == Bt_FormuleMagMax) Ed_FormuleMagMax.setText(sw.Ed_Commande.getText());
                    }
                    SaveClasses();
                    sw.dispose();
                }
            };
            Ed_FormuleAtt = new JTextField();
            Ed_FormuleAtt.setBounds(new Rectangle(170, 20, 360, 20));
            Ed_FormuleAtt.addKeyListener(keyListener);
            paneuneclasse.add(Ed_FormuleAtt);
            Bt_FormuleAtt = new JButton("...");
            Bt_FormuleAtt.setBounds(new Rectangle(531, 20, 18, 20));
            Bt_FormuleAtt.addActionListener(stwizard);
            paneuneclasse.add(Bt_FormuleAtt);
            Ed_FormuleEsq = new JTextField();
            Ed_FormuleEsq.setBounds(new Rectangle(170, 40, 360, 20));
            Ed_FormuleEsq.addKeyListener(keyListener);
            paneuneclasse.add(Ed_FormuleEsq);
            Bt_FormuleEsq = new JButton("...");
            Bt_FormuleEsq.setBounds(new Rectangle(531, 40, 18, 20));
            Bt_FormuleEsq.addActionListener(stwizard);
            paneuneclasse.add(Bt_FormuleEsq);
            Ed_FormuleDeg = new JTextField();
            Ed_FormuleDeg.setBounds(new Rectangle(170, 60, 360, 20));
            Ed_FormuleDeg.addKeyListener(keyListener);
            paneuneclasse.add(Ed_FormuleDeg);
            Bt_FormuleDeg = new JButton("...");
            Bt_FormuleDeg.setBounds(new Rectangle(531, 60, 18, 20));
            Bt_FormuleDeg.addActionListener(stwizard);
            paneuneclasse.add(Bt_FormuleDeg);
            Ed_FormuleDef = new JTextField();
            Ed_FormuleDef.setBounds(new Rectangle(170, 80, 360, 20));
            Ed_FormuleDef.addKeyListener(keyListener);
            paneuneclasse.add(Ed_FormuleDef);
            Bt_FormuleDef = new JButton("...");
            Bt_FormuleDef.setBounds(new Rectangle(531, 80, 18, 20));
            Bt_FormuleDef.addActionListener(stwizard);
            paneuneclasse.add(Bt_FormuleDef);
            Ed_FormuleXP = new JTextField();
            Ed_FormuleXP.setBounds(new Rectangle(170, 100, 360, 20));
            Ed_FormuleXP.addKeyListener(keyListener);
            paneuneclasse.add(Ed_FormuleXP);
            Bt_FormuleXP = new JButton("...");
            Bt_FormuleXP.setBounds(new Rectangle(531, 100, 18, 20));
            Bt_FormuleXP.addActionListener(stwizard);
            paneuneclasse.add(Bt_FormuleXP);
            Ed_FormuleGold = new JTextField();
            Ed_FormuleGold.setBounds(new Rectangle(170, 120, 360, 20));
            Ed_FormuleGold.addKeyListener(keyListener);
            paneuneclasse.add(Ed_FormuleGold);
            Bt_FormuleGold = new JButton("...");
            Bt_FormuleGold.setBounds(new Rectangle(531, 120, 18, 20));
            Bt_FormuleGold.addActionListener(stwizard);
            paneuneclasse.add(Bt_FormuleGold);
            Ed_FormuleVieMax = new JTextField();
            Ed_FormuleVieMax.setBounds(new Rectangle(170, 140, 360, 20));
            Ed_FormuleVieMax.addKeyListener(keyListener);
            paneuneclasse.add(Ed_FormuleVieMax);
            Bt_FormuleVieMax = new JButton("...");
            Bt_FormuleVieMax.setBounds(new Rectangle(531, 140, 18, 20));
            Bt_FormuleVieMax.addActionListener(stwizard);
            paneuneclasse.add(Bt_FormuleVieMax);
            Ed_FormuleMagMax = new JTextField();
            Ed_FormuleMagMax.setBounds(new Rectangle(170, 160, 360, 20));
            Ed_FormuleMagMax.addKeyListener(keyListener);
            paneuneclasse.add(Ed_FormuleMagMax);
            Bt_FormuleMagMax = new JButton("...");
            Bt_FormuleMagMax.setBounds(new Rectangle(531, 160, 18, 20));
            Bt_FormuleMagMax.addActionListener(stwizard);
            paneuneclasse.add(Bt_FormuleMagMax);
            Ed_SonAttaque = new JTextField();
            Ed_SonAttaque.setBounds(new Rectangle(170, 180, 215, 20));
            Ed_SonAttaque.addKeyListener(keyListener);
            paneuneclasse.add(Ed_SonAttaque);
            ActionListener ChooseSound = new ActionListener() {

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
                        SaveClasses();
                    }
                }
            };
            Bt_ChooseSonAttaque = new JButton("...");
            Bt_ChooseSonAttaque.setBounds(new Rectangle(386, 180, 18, 20));
            Bt_ChooseSonAttaque.addActionListener(ChooseSound);
            paneuneclasse.add(Bt_ChooseSonAttaque);
            Ed_LvlUpPoint = new JTextField();
            Ed_LvlUpPoint.setBounds(new Rectangle(490, 180, 40, 20));
            Ed_LvlUpPoint.addKeyListener(keyListener);
            paneuneclasse.add(Ed_LvlUpPoint);
            Ed_SonBlesse = new JTextField();
            Ed_SonBlesse.setBounds(new Rectangle(170, 200, 215, 20));
            Ed_SonBlesse.addKeyListener(keyListener);
            paneuneclasse.add(Ed_SonBlesse);
            Bt_ChooseSonBlesse = new JButton("...");
            Bt_ChooseSonBlesse.setBounds(new Rectangle(386, 200, 18, 20));
            Bt_ChooseSonBlesse.addActionListener(ChooseSound);
            paneuneclasse.add(Bt_ChooseSonBlesse);
            Ed_LvlMax = new JTextField();
            Ed_LvlMax.setBounds(new Rectangle(490, 200, 40, 20));
            Ed_LvlMax.addKeyListener(keyListener);
            paneuneclasse.add(Ed_LvlMax);
            Bt_ChooseSonMagie = new JButton("...");
            Bt_ChooseSonMagie.setBounds(new Rectangle(386, 220, 18, 20));
            Bt_ChooseSonMagie.addActionListener(ChooseSound);
            paneuneclasse.add(Bt_ChooseSonMagie);
            Ed_SonMagie = new JTextField();
            Ed_SonMagie.setBounds(new Rectangle(170, 220, 215, 20));
            Ed_SonMagie.addKeyListener(keyListener);
            paneuneclasse.add(Ed_SonMagie);
            StdGridModel std = new StdGridModel("Stats Min", null);
            StatsMinGrid = new JTable(std);
            StatsMinGrid.setRowSelectionAllowed(false);
            StatsMinGrid.setColumnSelectionAllowed(false);
            JScrollPane scrollpaneSMG = new JScrollPane(StatsMinGrid);
            StatsMinGrid.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            StatsMinGrid.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            StatsMinGrid.setRowHeight(16);
            for (int i = 0; i < StatsMinGrid.getColumnCount(); i++) {
                TableColumn col = StatsMinGrid.getColumnModel().getColumn(i);
                col.setPreferredWidth(50);
            }
            scrollpaneSMG.setBounds(new Rectangle(0, 245, 153, 200));
            paneuneclasse.add(scrollpaneSMG);
            std = new StdGridModel("Stats Max", null);
            StatsMaxGrid = new JTable(std);
            StatsMaxGrid.setRowSelectionAllowed(false);
            StatsMaxGrid.setColumnSelectionAllowed(false);
            JScrollPane scrollpaneSMaG = new JScrollPane(StatsMaxGrid);
            StatsMaxGrid.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            StatsMaxGrid.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            StatsMaxGrid.setRowHeight(16);
            for (int i = 0; i < StatsMaxGrid.getColumnCount(); i++) {
                TableColumn col = StatsMaxGrid.getColumnModel().getColumn(i);
                col.setPreferredWidth(50);
            }
            scrollpaneSMaG.setBounds(new Rectangle(300, 245, 153, 200));
            paneuneclasse.add(scrollpaneSMaG);
            ComponentAdapter listener = new ComponentAdapter() {

                public void componentResized(ComponentEvent evt) {
                    Component c = (Component) evt.getSource();
                    Dimension newSize = c.getSize();
                    scrollpaneclasslist.setBounds(new Rectangle(10, 10, 190, newSize.height - 125));
                }
            };
            parent.addComponentListener(listener);
        }
