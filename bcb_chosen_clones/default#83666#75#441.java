    public OngletObjets(FenetreSimple p) {
        setLayout(null);
        parent = p;
        Font font = new Font("MS Sans Serif", Font.PLAIN, 10);
        Objetmodel = new DefaultListModel();
        ObjetList = new JList(Objetmodel);
        ObjetList.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                if (ObjetList.getSelectedIndex() >= 0) {
                    paneunobjet.setVisible(true);
                    AllowSave = false;
                    CB_Classe.removeAllItems();
                    CB_Classe.addItem("Aucune");
                    for (int i = 0; i < parent.general.getClassesJoueur().size(); i++) CB_Classe.addItem(parent.general.getClassesJoueur().get(i).Name);
                    CB_MagieAssoc.removeAllItems();
                    CB_MagieAssoc.addItem("Aucune");
                    for (int i = 0; i < parent.general.getMagies().size(); i++) CB_MagieAssoc.addItem(parent.general.getMagieByIndex(i).Name);
                    StdGridModel std = new StdGridModel("Stats Min", parent.general.getObjetByIndex(ObjetList.getSelectedIndex()).StatsMin);
                    StatsMinGrid.setModel(std);
                    StatsMinGrid.updateUI();
                    std = new StdGridModel("Stats", parent.general.getObjetByIndex(ObjetList.getSelectedIndex()).Stats);
                    StatsGrid.setModel(std);
                    StatsGrid.updateUI();
                    Ed_Nom.setText(parent.general.getObjetByIndex(ObjetList.getSelectedIndex()).Name);
                    Ed_Explication.setText(parent.general.getObjetByIndex(ObjetList.getSelectedIndex()).Explication);
                    Ed_Chipset.setText(parent.general.getObjetByIndex(ObjetList.getSelectedIndex()).Chipset);
                    Ed_X.setText(Integer.toString(parent.general.getObjetByIndex(ObjetList.getSelectedIndex()).X));
                    Ed_Y.setText(Integer.toString(parent.general.getObjetByIndex(ObjetList.getSelectedIndex()).Y));
                    Ed_W.setText(Integer.toString(parent.general.getObjetByIndex(ObjetList.getSelectedIndex()).W));
                    Ed_H.setText(Integer.toString(parent.general.getObjetByIndex(ObjetList.getSelectedIndex()).H));
                    Ed_Prix.setText(Integer.toString(parent.general.getObjetByIndex(ObjetList.getSelectedIndex()).Prix));
                    Ed_LvlMin.setText(Integer.toString(parent.general.getObjetByIndex(ObjetList.getSelectedIndex()).LvlMin));
                    Ed_Attaque.setText(Integer.toString(parent.general.getObjetByIndex(ObjetList.getSelectedIndex()).Attaque));
                    Ed_Defense.setText(Integer.toString(parent.general.getObjetByIndex(ObjetList.getSelectedIndex()).Defense));
                    Ed_PV.setText(Integer.toString(parent.general.getObjetByIndex(ObjetList.getSelectedIndex()).PV));
                    Ed_PM.setText(Integer.toString(parent.general.getObjetByIndex(ObjetList.getSelectedIndex()).PM));
                    CB_Classe.setSelectedIndex(parent.general.getObjetByIndex(ObjetList.getSelectedIndex()).Classe);
                    switch(parent.general.getObjetByIndex(ObjetList.getSelectedIndex()).ObjType) {
                        case 0:
                            RB_Utilise.setSelected(true);
                            break;
                        case 1:
                            RB_Armeunemain.setSelected(true);
                            break;
                        case 2:
                            RB_Armedeuxmain.setSelected(true);
                            break;
                        case 3:
                            RB_Casque.setSelected(true);
                            break;
                        case 4:
                            RB_Armure.setSelected(true);
                            break;
                        case 5:
                            RB_Bouclier.setSelected(true);
                            break;
                        case 6:
                            RB_Objetquete.setSelected(true);
                            break;
                        case 7:
                            RB_Livremagie.setSelected(true);
                            break;
                    }
                    LoadImage();
                    AllowSave = true;
                }
            }
        });
        scrollpaneobjlist = new JScrollPane(ObjetList);
        scrollpaneobjlist.setBounds(new Rectangle(10, 10, 190, 470));
        add(scrollpaneobjlist);
        JButton Bt_AjouteObjet = new JButton("Ajouter un objet");
        Bt_AjouteObjet.setBounds(new Rectangle(205, 10, 180, 20));
        add(Bt_AjouteObjet);
        Bt_AjouteObjet.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String NomObjet = JOptionPane.showInputDialog(null, "Entrez le nom du nouvel objet", "", 1);
                if (NomObjet != null) {
                    if (NomObjet.compareTo("") != 0) {
                        Objetmodel.add(ObjetList.getModel().getSize(), NomObjet);
                        parent.general.getObjets().add(parent.general.new Objet(NomObjet));
                        ArrayList<Integer> statsmin = parent.general.getObjetByIndex(parent.general.getObjets().size() - 1).StatsMin;
                        ArrayList<Integer> stats = parent.general.getObjetByIndex(parent.general.getObjets().size() - 1).Stats;
                        for (int i = 0; i < parent.general.getStatsBase().size(); i++) {
                            statsmin.add(0);
                            stats.add(0);
                        }
                        parent.monstres.StatsBaseChange();
                    }
                }
            }
        });
        JButton Bt_RetireObjet = new JButton("Retirer un objet");
        Bt_RetireObjet.setBounds(new Rectangle(400, 10, 180, 20));
        Bt_RetireObjet.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (ObjetList.getSelectedIndex() >= 0) {
                    if (JOptionPane.showConfirmDialog(null, "Etes vous sûr de vouloir effacer cet objet?", "Effacer", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                        paneunobjet.setVisible(false);
                        for (int i = 0; i < parent.general.getMonstres().size(); i++) {
                            for (int j = 0; j < parent.general.getMonstreByIndex(i).ObjectWin.size(); j++) {
                                if (parent.general.getMonstreByIndex(i).ObjectWin.get(j) > ObjetList.getSelectedIndex()) parent.general.getMonstreByIndex(i).ObjectWin.set(j, (short) (parent.general.getMonstreByIndex(i).ObjectWin.get(j) - 1));
                            }
                        }
                        parent.general.getObjets().remove(ObjetList.getSelectedIndex());
                        Objetmodel.remove(ObjetList.getSelectedIndex());
                        parent.monstres.StatsBaseChange();
                    }
                }
            }
        });
        add(Bt_RetireObjet);
        paneunobjet = new JPanel();
        paneunobjet.setLayout(null);
        paneunobjet.setBounds(new Rectangle(205, 35, 580, 550));
        paneunobjet.setVisible(false);
        add(paneunobjet);
        paneimg = new JLabel();
        paneimg.setBorder(BorderFactory.createLoweredBevelBorder());
        paneimg.setLayout(null);
        paneimg.setBounds(new Rectangle(0, 0, 112, 80));
        paneimg.setVerticalAlignment(JLabel.TOP);
        paneimg.setHorizontalAlignment(JLabel.LEFT);
        paneunobjet.add(paneimg);
        JLabel NomObjet = new JLabel("Nom : ");
        NomObjet.setBounds(new Rectangle(120, 0, 200, 20));
        paneunobjet.add(NomObjet);
        JLabel Explication = new JLabel("Explication : ");
        Explication.setBounds(new Rectangle(120, 20, 200, 20));
        paneunobjet.add(Explication);
        JLabel Chipset = new JLabel("Chipset : ");
        Chipset.setBounds(new Rectangle(120, 40, 200, 20));
        paneunobjet.add(Chipset);
        KeyListener keyListener = new KeyListener() {

            public void keyTyped(KeyEvent keyEvent) {
            }

            public void keyPressed(KeyEvent keyEvent) {
            }

            public void keyReleased(KeyEvent keyEvent) {
                if ((keyEvent.getSource().equals(Ed_X)) || (keyEvent.getSource().equals(Ed_Y)) || (keyEvent.getSource().equals(Ed_W)) || (keyEvent.getSource().equals(Ed_H))) RefreshImage();
                SaveObjet();
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
                parent.general.getObjetByIndex(ObjetList.getSelectedIndex()).Name = Ed_Nom.getText();
                Objetmodel.set(ObjetList.getSelectedIndex(), Ed_Nom.getText());
            }
        });
        paneunobjet.add(Ed_Nom);
        Ed_Explication = new JTextField();
        Ed_Explication.setBounds(new Rectangle(210, 20, 340, 20));
        Ed_Explication.addKeyListener(keyListener);
        paneunobjet.add(Ed_Explication);
        Ed_Chipset = new JTextField();
        Ed_Chipset.setBounds(new Rectangle(210, 40, 260, 20));
        Ed_Chipset.addKeyListener(keyListener);
        paneunobjet.add(Ed_Chipset);
        JButton Bt_ChooseChipset = new JButton("...");
        Bt_ChooseChipset.setBounds(new Rectangle(471, 40, 18, 20));
        Bt_ChooseChipset.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JFileChooser choix = new JFileChooser();
                choix.addChoosableFileFilter(parent.new FilterImage());
                choix.setCurrentDirectory(new java.io.File(parent.NomCarte + "/Chipset"));
                int retour = choix.showOpenDialog(null);
                if (retour == JFileChooser.APPROVE_OPTION) {
                    if (!new File(parent.NomCarte + "/Chipset/" + choix.getSelectedFile().getName()).exists()) parent.copyfile(choix.getSelectedFile().getAbsolutePath(), parent.NomCarte + "/Chipset/" + choix.getSelectedFile().getName());
                    Ed_Chipset.setText("Chipset\\" + choix.getSelectedFile().getName());
                    SaveObjet();
                    LoadImage();
                }
            }
        });
        paneunobjet.add(Bt_ChooseChipset);
        JLabel Lbl_X = new JLabel("X :");
        Lbl_X.setFont(font);
        Lbl_X.setBounds(new Rectangle(0, 90, 30, 17));
        paneunobjet.add(Lbl_X);
        Ed_X = new JTextField();
        Ed_X.setFont(font);
        Ed_X.setBounds(new Rectangle(20, 90, 33, 17));
        Ed_X.addKeyListener(keyListener);
        paneunobjet.add(Ed_X);
        JLabel Lbl_Y = new JLabel("Y :");
        Lbl_Y.setFont(font);
        Lbl_Y.setBounds(new Rectangle(60, 90, 30, 17));
        paneunobjet.add(Lbl_Y);
        Ed_Y = new JTextField();
        Ed_Y.setFont(font);
        Ed_Y.setBounds(new Rectangle(80, 90, 33, 17));
        Ed_Y.addKeyListener(keyListener);
        paneunobjet.add(Ed_Y);
        JLabel Lbl_W = new JLabel("W :");
        Lbl_W.setFont(font);
        Lbl_W.setBounds(new Rectangle(0, 112, 30, 17));
        paneunobjet.add(Lbl_W);
        Ed_W = new JTextField();
        Ed_W.setFont(font);
        Ed_W.setBounds(new Rectangle(20, 112, 33, 17));
        Ed_W.addKeyListener(keyListener);
        paneunobjet.add(Ed_W);
        JLabel Lbl_H = new JLabel("H :");
        Lbl_H.setFont(font);
        Lbl_H.setBounds(new Rectangle(60, 112, 30, 17));
        paneunobjet.add(Lbl_H);
        Ed_H = new JTextField();
        Ed_H.setFont(font);
        Ed_H.setBounds(new Rectangle(80, 112, 33, 17));
        Ed_H.addKeyListener(keyListener);
        paneunobjet.add(Ed_H);
        JLabel Lbl_Prix = new JLabel("Prix : ");
        Lbl_Prix.setBounds(new Rectangle(120, 60, 200, 20));
        paneunobjet.add(Lbl_Prix);
        Ed_Prix = new JTextField();
        Ed_Prix.setBounds(new Rectangle(210, 60, 90, 20));
        Ed_Prix.addKeyListener(keyListener);
        paneunobjet.add(Ed_Prix);
        JLabel Lbl_LvlMin = new JLabel("Lvl min : ");
        Lbl_LvlMin.setBounds(new Rectangle(305, 60, 200, 20));
        paneunobjet.add(Lbl_LvlMin);
        Ed_LvlMin = new JTextField();
        Ed_LvlMin.setBounds(new Rectangle(380, 60, 90, 20));
        Ed_LvlMin.addKeyListener(keyListener);
        paneunobjet.add(Ed_LvlMin);
        JLabel Lbl_Attaque = new JLabel("Attaque : ");
        Lbl_Attaque.setBounds(new Rectangle(120, 80, 200, 20));
        paneunobjet.add(Lbl_Attaque);
        Ed_Attaque = new JTextField();
        Ed_Attaque.setBounds(new Rectangle(210, 80, 90, 20));
        Ed_Attaque.addKeyListener(keyListener);
        paneunobjet.add(Ed_Attaque);
        JLabel Lbl_Defense = new JLabel("Defense : ");
        Lbl_Defense.setBounds(new Rectangle(305, 80, 200, 20));
        paneunobjet.add(Lbl_Defense);
        Ed_Defense = new JTextField();
        Ed_Defense.setBounds(new Rectangle(380, 80, 90, 20));
        Ed_Defense.addKeyListener(keyListener);
        paneunobjet.add(Ed_Defense);
        JLabel Lbl_Pv = new JLabel("Vie : ");
        Lbl_Pv.setBounds(new Rectangle(120, 100, 200, 20));
        paneunobjet.add(Lbl_Pv);
        Ed_PV = new JTextField();
        Ed_PV.setBounds(new Rectangle(210, 100, 90, 20));
        Ed_PV.addKeyListener(keyListener);
        paneunobjet.add(Ed_PV);
        JLabel Lbl_Pm = new JLabel("Magie : ");
        Lbl_Pm.setBounds(new Rectangle(305, 100, 200, 20));
        paneunobjet.add(Lbl_Pm);
        Ed_PM = new JTextField();
        Ed_PM.setBounds(new Rectangle(380, 100, 90, 20));
        Ed_PM.addKeyListener(keyListener);
        paneunobjet.add(Ed_PM);
        JLabel Lbl_Classe = new JLabel("Classe : ");
        Lbl_Classe.setBounds(new Rectangle(120, 120, 200, 20));
        paneunobjet.add(Lbl_Classe);
        CB_Classe = new JComboBox();
        CB_Classe.setBounds(new Rectangle(210, 120, 260, 20));
        ActionListener ac = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                SaveObjet();
            }
        };
        CB_Classe.addActionListener(ac);
        paneunobjet.add(CB_Classe);
        JLabel Lbl_MagieAssoc = new JLabel("Magie : ");
        Lbl_MagieAssoc.setBounds(new Rectangle(120, 140, 200, 20));
        paneunobjet.add(Lbl_MagieAssoc);
        CB_MagieAssoc = new JComboBox();
        CB_MagieAssoc.setBounds(new Rectangle(210, 140, 260, 20));
        CB_MagieAssoc.addActionListener(ac);
        paneunobjet.add(CB_MagieAssoc);
        JPanel panetypeobj = new JPanel();
        TitledBorder title = BorderFactory.createTitledBorder("Type d'objet");
        title.setTitleFont(font);
        panetypeobj.setBorder(title);
        panetypeobj.setBounds(new Rectangle(0, 180, 170, 200));
        panetypeobj.setLayout(new GridLayout(0, 1));
        paneunobjet.add(panetypeobj);
        ButtonGroup group = new ButtonGroup();
        RB_Utilise = new JRadioButton("S'utilise");
        RB_Utilise.addActionListener(ac);
        group.add(RB_Utilise);
        panetypeobj.add(RB_Utilise);
        RB_Armeunemain = new JRadioButton("Arme à une main");
        RB_Armeunemain.addActionListener(ac);
        group.add(RB_Armeunemain);
        panetypeobj.add(RB_Armeunemain);
        RB_Armedeuxmain = new JRadioButton("Arme à deux mains");
        RB_Armedeuxmain.addActionListener(ac);
        group.add(RB_Armedeuxmain);
        panetypeobj.add(RB_Armedeuxmain);
        RB_Casque = new JRadioButton("Casque");
        RB_Casque.addActionListener(ac);
        group.add(RB_Casque);
        panetypeobj.add(RB_Casque);
        RB_Armure = new JRadioButton("Armure");
        RB_Armure.addActionListener(ac);
        group.add(RB_Armure);
        panetypeobj.add(RB_Armure);
        RB_Bouclier = new JRadioButton("Bouclier");
        RB_Bouclier.addActionListener(ac);
        group.add(RB_Bouclier);
        panetypeobj.add(RB_Bouclier);
        RB_Objetquete = new JRadioButton("Objet de quête");
        RB_Objetquete.addActionListener(ac);
        group.add(RB_Objetquete);
        panetypeobj.add(RB_Objetquete);
        RB_Livremagie = new JRadioButton("Livre de magie");
        RB_Livremagie.addActionListener(ac);
        group.add(RB_Livremagie);
        panetypeobj.add(RB_Livremagie);
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
        scrollpaneSMG.setBounds(new Rectangle(200, 180, 153, 200));
        paneunobjet.add(scrollpaneSMG);
        std = new StdGridModel("Stats", null);
        StatsGrid = new JTable(std);
        StatsGrid.setRowSelectionAllowed(false);
        StatsGrid.setColumnSelectionAllowed(false);
        JScrollPane scrollpaneSG = new JScrollPane(StatsGrid);
        StatsGrid.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        StatsGrid.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        StatsGrid.setRowHeight(16);
        for (int i = 0; i < StatsGrid.getColumnCount(); i++) {
            TableColumn col = StatsGrid.getColumnModel().getColumn(i);
            col.setPreferredWidth(50);
        }
        scrollpaneSG.setBounds(new Rectangle(400, 180, 153, 200));
        paneunobjet.add(scrollpaneSG);
        ComponentAdapter listener = new ComponentAdapter() {

            public void componentResized(ComponentEvent evt) {
                Component c = (Component) evt.getSource();
                Dimension newSize = c.getSize();
                scrollpaneobjlist.setBounds(new Rectangle(10, 10, 190, newSize.height - 105));
            }
        };
        parent.addComponentListener(listener);
    }
