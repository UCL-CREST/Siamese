        public Contenu(Projet prj, CommandeEv p, JTextField Ed_C) {
            parent = p;
            projet = prj;
            Ed_Commande = Ed_C;
            setLayout(null);
            ListCommande = new JList(new String[] { "Message('Entrez votre message retour a la ligne automatique')", "Condition('Appuie sur bouton')", "AddObject(NomObjet)", "DelObject(NomObjet)", "Teleport(NomCarte,CaseX,CaseY)", "ChangeResPoint(NomCarte,CaseX,CaseY)", "SScroll(CaseX,CaseY)", "ChangeClasse('Classe')", "ChangeSkin('Chipset\\skin.png')", "GenereMonstre(NomMonstre,CaseX,CaseY,NbMonstre,Respawn,DonneXP)", "TueMonstre", "InputQuery('Voulez vous dormir ici?','oui','non','5 choix possible','vide=inutilise')", "OnResultQuery('oui')", "QueryEnd", "InputString('Entrez votre texte ici')", "Magasin('Bonjour,que puis je faire pour vous?','Objet1','Objet2')", "Attente(Temps)", "PlayMusic('Sound\\nom.mid')", "StopMusic", "PlaySound('Sound\\sound.wav')", "ChAttaqueSound('Sound\\sound.wav')", "ChBlesseSound('Sound\\sound.wav')", "AddMagie(NomMagie)", "DelMagie(NomMagie)", "Concat('Chaine')", "// Commentaires", "Chargement('nom')", "Sauvegarde('nom')", "Quitter()", "Options()", "ShowInterface", "HideInterface", "ReinitPlayer()", "AddMenu(Menu)", "DelMenu(Menu)" });
            JScrollPane lc = new JScrollPane(ListCommande);
            lc.setBounds(new Rectangle(6, 22, 205, 280));
            ListCommande.addMouseListener(new MouseAdapter() {

                public void mousePressed(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        String S, S2, S3, Temp;
                        int i;
                        String[] projstr;
                        switch(ListCommande.getSelectedIndex()) {
                            case 0:
                            case 12:
                                S = JOptionPane.showInputDialog(null, "Entrez le message", "Message", 1);
                                if (S != null) {
                                    S = "'" + S;
                                    if (ListCommande.getSelectedIndex() == 0) {
                                        Temp = "";
                                        if (JOptionPane.showConfirmDialog(null, "Voulez vous positionner le message?", "Option", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                                            jump = new JumpTo(null, true);
                                            Temp = jump.Ed_X.getText() + "," + jump.Ed_Y.getText();
                                            jump.dispose();
                                            jump = new JumpTo(null, false);
                                            jump.setVisible(false);
                                            jump.setModal(true);
                                            jump.setTitle("Largeur/Hauteur");
                                            jump.LblX.setText("W");
                                            jump.LblY.setText("H");
                                            jump.setVisible(true);
                                            Temp += "," + jump.Ed_X.getText() + "," + jump.Ed_Y.getText() + ",";
                                            jump.dispose();
                                            S = Temp + S;
                                        }
                                        Ed_Commande.setText("Message(" + S + "')");
                                    }
                                    if (ListCommande.getSelectedIndex() == 12) Ed_Commande.setText("OnResultQuery('" + S + "')");
                                }
                                break;
                            case 1:
                                cd = new CondDecl(projet, "", null, true);
                                if (cd.status == 1) Ed_Commande.setText("Condition('" + cd.Commande + "')");
                                cd.dispose();
                                break;
                            case 2:
                            case 3:
                                values = new String[projet.getObjets().size()];
                                for (int j = 0; j < projet.getObjets().size(); j++) values[j] = projet.getObjetByIndex(j).Name;
                                liste = new JListe(values, null, "Choisissez l'objet", true);
                                if (liste.status == 1) {
                                    S = "1";
                                    S = (String) JOptionPane.showInputDialog(null, "Entrez la quantité de l'objet", "Quantité", 1, null, null, S);
                                    if (S != null) {
                                        if (S.compareTo("") != 0) {
                                            if (S.compareTo("1") == 0) {
                                                if (ListCommande.getSelectedIndex() == 2) Ed_Commande.setText("AddObject(" + liste.ListBox.getSelectedValue().toString() + ")"); else Ed_Commande.setText("DelObject(" + liste.ListBox.getSelectedValue().toString() + ")");
                                            } else {
                                                if (ListCommande.getSelectedIndex() == 2) Ed_Commande.setText("AddObject(" + liste.ListBox.getSelectedValue().toString() + "," + S + ")"); else Ed_Commande.setText("DelObject(" + liste.ListBox.getSelectedValue().toString() + "," + S + ")");
                                            }
                                        }
                                    }
                                }
                                liste.dispose();
                                break;
                            case 4:
                            case 5:
                                ArrayList<Carte> carte = projet.getCartes();
                                ArrayList<String> nomcarte = new ArrayList<String>();
                                for (i = 0; i < carte.size(); i++) nomcarte.add(carte.get(i).Name);
                                projstr = new String[nomcarte.size()];
                                projstr = nomcarte.toArray(projstr);
                                liste = new JListe(projstr, null, "Choisissez la carte", true);
                                if (liste.status == 1) {
                                    jump = new JumpTo(null, true);
                                    if (jump.status == 1) {
                                        if (ListCommande.getSelectedIndex() == 4) Ed_Commande.setText("Teleport(" + liste.ListBox.getSelectedValue().toString() + "," + jump.Ed_X.getText() + "," + jump.Ed_Y.getText() + ")"); else Ed_Commande.setText("ChangeResPoint(" + liste.ListBox.getSelectedValue().toString() + "," + jump.Ed_X.getText() + "," + jump.Ed_Y.getText() + ")");
                                    }
                                    jump.dispose();
                                }
                                liste.dispose();
                                break;
                            case 6:
                                jump = new JumpTo(null, true);
                                if (jump.status == 1) Ed_Commande.setText("SScroll(" + jump.Ed_X.getText() + "," + jump.Ed_Y.getText() + ")");
                                jump.dispose();
                                break;
                            case 7:
                                ArrayList<String> nomclasses = new ArrayList<String>();
                                for (i = 0; i < projet.getClassesJoueur().size(); i++) nomclasses.add(projet.getClassesJoueur().get(i).Name);
                                projstr = new String[nomclasses.size()];
                                projstr = nomclasses.toArray(projstr);
                                liste = new JListe(projstr, null, "Choisissez la classe(vide=aucune)", true);
                                if (liste.status == 1) {
                                    Ed_Commande.setText("ChangeClasse('" + liste.ListBox.getSelectedValue().toString() + "')");
                                }
                                break;
                            case 8:
                            case 17:
                            case 19:
                            case 20:
                            case 21:
                                JFileChooser choix = new JFileChooser();
                                if (ListCommande.getSelectedIndex() == 7) choix.setCurrentDirectory(new java.io.File(projet.getName() + "/Chipset/")); else choix.setCurrentDirectory(new java.io.File(projet.getName() + "/Sound/"));
                                int retour = choix.showOpenDialog(null);
                                if (retour == JFileChooser.APPROVE_OPTION) {
                                    switch(ListCommande.getSelectedIndex()) {
                                        case 8:
                                            Ed_Commande.setText("ChangeSkin('Chipset\\" + choix.getSelectedFile().getName() + "')");
                                            break;
                                        case 17:
                                            Ed_Commande.setText("PlayMusic('Sound\\" + choix.getSelectedFile().getName() + "')");
                                            break;
                                        case 29:
                                            Ed_Commande.setText("PlaySound('Sound\\" + choix.getSelectedFile().getName() + "')");
                                            break;
                                        case 20:
                                            Ed_Commande.setText("ChAttaqueSound('Sound\\" + choix.getSelectedFile().getName() + "')");
                                            break;
                                        case 21:
                                            Ed_Commande.setText("ChBlesseSound('Sound\\" + choix.getSelectedFile().getName() + "')");
                                            break;
                                    }
                                }
                                break;
                            case 9:
                                values = new String[projet.getMonstres().size()];
                                for (int j = 0; j < projet.getMonstres().size(); j++) values[j] = projet.getMonstreByIndex(j).Name;
                                liste = new JListe(values, null, "Choisissez le monstre", true);
                                if (liste.status == 1) {
                                    jump = new JumpTo(null, true);
                                    if (jump.status == 1) {
                                        S = JOptionPane.showInputDialog(null, "Entrez le nombre de Monstre", "Monstre", 1);
                                        if (S != null) {
                                            S2 = JOptionPane.showInputDialog(null, "Vitesse de respawn?(0=ne respawn pas)", "Monstre", 1);
                                            if (S2 != null) {
                                                S3 = JOptionPane.showInputDialog(null, "Monstres donnent de l'xp? (0=non, 1=oui)", "Monstre", 1);
                                                if (S3 != null) {
                                                    Ed_Commande.setText("GenereMonstre(" + liste.ListBox.getSelectedValue().toString() + "," + jump.Ed_X.getText() + "," + jump.Ed_Y.getText() + "," + S + "," + S2 + "," + S3 + ")");
                                                }
                                            }
                                        }
                                    }
                                    jump.dispose();
                                }
                                liste.dispose();
                                break;
                            case 11:
                                Temp = "";
                                if (JOptionPane.showConfirmDialog(null, "Voulez vous positionner le query?", "Option", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                                    jump = new JumpTo(null, true);
                                    if (jump.status == 1) Temp = "InputQuery(" + jump.Ed_X.getText() + "," + jump.Ed_Y.getText();
                                    jump.dispose();
                                }
                                S = JOptionPane.showInputDialog(null, "Entrez la question", "Message", 1);
                                if (S != null) {
                                    i = 0;
                                    if (Temp.compareTo("") == 0) Temp = "InputQuery('" + S + "'"; else Temp += ",'" + S + "'";
                                    do {
                                        S = "";
                                        S = JOptionPane.showInputDialog(null, "Entrez la réponse " + (i + 1), "Message", 1);
                                        if (S == null) S = "";
                                        if (S != "") Temp += ",'" + S + "'";
                                        i++;
                                    } while (S != "");
                                    Temp += ")";
                                    Ed_Commande.setText(Temp);
                                }
                                break;
                            case 14:
                                S = JOptionPane.showInputDialog(null, "Entrez la question", "Message", 1);
                                if (S != null) Ed_Commande.setText(Ed_Commande.getText() + "InputString('" + S + "')");
                                break;
                            case 15:
                                S = JOptionPane.showInputDialog(null, "Entrez le message du magasin", "Message", 1);
                                if (S != null) {
                                    values = new String[projet.getObjets().size()];
                                    for (int j = 0; j < projet.getObjets().size(); j++) values[j] = projet.getObjetByIndex(j).Name;
                                    liste = new JListe(values, null, "Choisissez les objets", true);
                                    if (liste.status == 1) {
                                        Ed_Commande.setText("Magasin('" + S + "'");
                                        Object[] obj = liste.ListBox.getSelectedValues();
                                        for (int j = 0; j < obj.length; j++) Ed_Commande.setText(Ed_Commande.getText() + ",'" + obj[j].toString() + "'");
                                        Ed_Commande.setText(Ed_Commande.getText() + ")");
                                    }
                                    liste.dispose();
                                }
                                break;
                            case 16:
                                S = JOptionPane.showInputDialog(null, "Entrez le temps d'attente", "Timer", 1);
                                if (S != null) Ed_Commande.setText("Attente(" + S + ")");
                                break;
                            case 22:
                            case 23:
                                values = new String[projet.getMagies().size()];
                                for (int j = 0; j < projet.getMagies().size(); j++) values[j] = projet.getMagieByIndex(j).Name;
                                liste = new JListe(values, null, "Choisissez la magie", true);
                                if (liste.status == 1) {
                                    if (ListCommande.getSelectedIndex() == 23) Ed_Commande.setText("AddMagie(" + liste.ListBox.getSelectedValue().toString() + ")"); else Ed_Commande.setText("DelMagie(" + liste.ListBox.getSelectedValue().toString() + ")");
                                }
                                liste.dispose();
                                break;
                            case 26:
                            case 27:
                                S = JOptionPane.showInputDialog(null, "Entrez le nom de la sauvegarde(Vide = Choix du joueur)", "Sauvegarde", 1);
                                if (S != null) {
                                    if (ListCommande.getSelectedIndex() == 27) Ed_Commande.setText("Chargement('" + S + "')"); else Ed_Commande.setText("Sauvegarde('" + S + "')");
                                }
                                break;
                            case 33:
                            case 34:
                                projstr = new String[verifie.getMenuPossibles().size()];
                                projstr = verifie.getMenuPossibles().toArray(projstr);
                                liste = new JListe(projstr, null, "Choisissez le menu", true);
                                if (liste.status == 1) {
                                    if (ListCommande.getSelectedIndex() == 32) Ed_Commande.setText("AddMenu(" + liste.ListBox.getSelectedValue().toString() + ")"); else Ed_Commande.setText("DelMenu(" + liste.ListBox.getSelectedValue().toString() + ")");
                                }
                                break;
                            default:
                                Ed_Commande.setText(Ed_Commande.getText() + ListCommande.getSelectedValue().toString());
                        }
                    }
                }
            });
            add(lc);
            ListEvent = new JList(new String[] { "%NomEv.Name%", "%NomEv.CaseX%", "%NomEv.CaseY%", "%NomEv.CaseNBX%", "%NomEv.CaseNBY%", "%NomEv.Chipset%", "%NomEv.Bloquant%", "%NomEv.Transparent%", "%NomEv.Visible%", "%NomEv.TypeAnim%", "%NomEv.Direction%", "%NomEv.X%", "%NomEv.Y%", "%NomEv.W%", "%NomEv.H%", "%NomEv.NumAnim%", "%NomEv.Vitesse%", "%NomEv.AnimAttaque%", "%NomEv.AnimDefense%", "%NomEv.AnimMagie%" });
            JScrollPane le = new JScrollPane(ListEvent);
            le.setBounds(new Rectangle(220, 22, 170, 208));
            ListEvent.addMouseListener(new MouseAdapter() {

                public void mousePressed(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        String S;
                        S = JOptionPane.showInputDialog(null, "Entrez le nom de l'événement", "Evénement", 1);
                        if (S != null) Ed_Commande.setText(ListEvent.getSelectedValue().toString().replaceAll("NomEv", S) + "=");
                    }
                }
            });
            add(le);
            values = new String[projet.getObjets().size()];
            for (int j = 0; j < projet.getObjets().size(); j++) values[j] = projet.getObjetByIndex(j).Name;
            ListObj = new JComboBox(values);
            ListObj.setBounds(new Rectangle(220, 248, 170, 20));
            ListObj.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    Ed_Commande.setText(Ed_Commande.getText() + projet.getObjetByIndex(ListObj.getSelectedIndex()).Name);
                }
            });
            add(ListObj);
            values = new String[projet.getMagies().size()];
            for (int j = 0; j < projet.getMagies().size(); j++) values[j] = projet.getMagieByIndex(j).Name;
            ListMag = new JComboBox(values);
            ListMag.setBounds(new Rectangle(220, 283, 170, 20));
            ListMag.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    Ed_Commande.setText(Ed_Commande.getText() + projet.getMagieByIndex(ListMag.getSelectedIndex()).Name);
                }
            });
            add(ListMag);
            values = new String[35 + projet.getStatsBase().size()];
            values[0] = "%Name%";
            values[1] = "%UpperName%";
            values[2] = "%Classe%";
            values[3] = "%Skin%";
            values[4] = "%Vie%";
            values[5] = "%VieMax%";
            values[6] = "%CurrentMag%";
            values[7] = "%MagMax%";
            values[8] = "%Gold%";
            values[9] = "%Lvl%";
            values[10] = "%LvlPoint%";
            values[11] = "%CurrentXP%";
            values[12] = "%NextXP%";
            values[13] = "%Timer%";
            values[14] = "%Timer2%";
            values[15] = "%Timer3%";
            values[16] = "%Effect%";
            values[17] = "%upper(chaine)%";
            values[18] = "%rand(100)%";
            values[19] = "%max(valeur1,valeur2)%";
            values[20] = "%min(valeur1,valeur2)%";
            values[21] = "%Visible%";
            values[22] = "%Bloque%";
            values[23] = "%CaseX%";
            values[24] = "%CaseY%";
            values[25] = "%Position%";
            values[26] = "%CentreX%";
            values[27] = "%CentreY%";
            values[28] = "%BloqueChangeSkin%";
            values[29] = "%BloqueAttaque%";
            values[30] = "%BloqueDefense%";
            values[31] = "%BloqueMagie%";
            values[32] = "%NbObjetInventaire%";
            values[33] = "%Direction%";
            for (int i = 0; i < projet.getStatsBase().size(); i++) values[34 + i] = "%" + projet.getStatsBase().get(i) + "%";
            values[34 + projet.getStatsBase().size()] = "Variable[nomvar]";
            ListJoueur = new JList(values);
            JScrollPane lj = new JScrollPane(ListJoueur);
            lj.setBounds(new Rectangle(400, 22, 150, 280));
            ListJoueur.addMouseListener(new MouseAdapter() {

                public void mousePressed(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        Ed_Commande.setText(Ed_Commande.getText() + ListJoueur.getSelectedValue().toString());
                    }
                }
            });
            add(lj);
            Ed_Commande.setBounds(new Rectangle(6, 320, 545, 20));
            Ed_Commande.addKeyListener(new KeyListener() {

                public void keyTyped(KeyEvent keyEvent) {
                }

                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) Bt_Ok.doClick();
                }

                public void keyReleased(KeyEvent e) {
                }
            });
            add(Ed_Commande);
            Bt_Ok = new JButton("Ok");
            Bt_Ok.setBounds(new Rectangle(6, 345, 90, 20));
            add(Bt_Ok);
            Bt_Annuler = new JButton("Annuler");
            Bt_Annuler.setBounds(new Rectangle(100, 345, 90, 20));
            add(Bt_Annuler);
        }
