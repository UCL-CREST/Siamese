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
