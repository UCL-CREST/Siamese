    public void actionPerformed(ActionEvent telleAction) {
        JMenuItem laSource = (JMenuItem) (telleAction.getSource());
        String leChoix = laSource.getText();
        if (leChoix == "Nouveau") {
            UneCarte laCarte = new UneCarte(this, this);
            if (!laCarte.getsonNom().equals("")) {
                saCarte = laCarte;
                afficheLaCarte();
                afficheEnContexte(new JPanel());
                sonTypeSelectionne = saCarte.getsonType();
                getJMenuBar().getMenu(2).setEnabled(true);
                getJMenuBar().getMenu(1).setEnabled(true);
                getJMenuBar().updateUI();
            }
        }
        if (leChoix == "Ouvrir") {
            int leRetour = sonSelectionneurDeFichier.showOpenDialog(UnEditeurDonjon.this);
            if (leRetour == JFileChooser.APPROVE_OPTION) {
                File leFichier = sonSelectionneurDeFichier.getSelectedFile();
                UneCarte laCarte = new UneCarte(leFichier, this);
                if (!laCarte.getsonNom().equals("")) {
                    saCarte = laCarte;
                    afficheLaCarte();
                    afficheEnContexte(new JPanel());
                    sonTypeSelectionne = saCarte.getsonType();
                    getJMenuBar().getMenu(2).setEnabled(true);
                    getJMenuBar().getMenu(1).setEnabled(true);
                    getJMenuBar().updateUI();
                }
            }
        }
        if (leChoix == "Enregistrer") {
            int leRetour = sonSelectionneurDeFichier.showSaveDialog(UnEditeurDonjon.this);
            if (leRetour == JFileChooser.APPROVE_OPTION) {
                File leFichier = sonSelectionneurDeFichier.getSelectedFile();
                saCarte.enregistreToi(leFichier);
            }
        }
        if (leChoix == "Quitter") {
            int laDecision = JOptionPane.showConfirmDialog(this, "D�sirez-vous sauvegarder avant de quitter?\n", "Quitter", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (laDecision == JOptionPane.YES_OPTION) {
                int leRetour = sonSelectionneurDeFichier.showSaveDialog(UnEditeurDonjon.this);
                if (leRetour == JFileChooser.APPROVE_OPTION) {
                    File leFichier = sonSelectionneurDeFichier.getSelectedFile();
                    saCarte.enregistreToi(leFichier);
                }
                System.exit(0);
            }
            if (laDecision == JOptionPane.NO_OPTION) {
                System.exit(0);
            }
        }
        if (leChoix == "Cr�er un personnage joueur") {
            afficheEnContexte(new UnJoueur(this).afficheToi());
        }
        if (leChoix == "Cr�er un personnage non-joueur") {
            afficheEnContexte(new UnNonJoueur(this).afficheToi());
        }
        if (leChoix == "Cr�er un objet simple") {
            afficheEnContexte(new UnObjet().afficheToi());
        }
        if (leChoix == "Cr�er une armure") {
            afficheEnContexte(new UneArmure().afficheToi());
        }
        if (leChoix == "Cr�er une arme") {
            afficheEnContexte(new UneArme().afficheToi());
        }
        if (leChoix == "Cr�er un consommable") {
            afficheEnContexte(new UnConsommable().afficheToi());
        }
        if (leChoix == "Cr�er un type de case") {
            afficheEnContexte(new UnType());
        }
        if (leChoix == "Cr�er une classe") {
            afficheEnContexte(new UneClasse().afficheToi());
        }
        if (leChoix == "Cr�er un sort") {
            afficheEnContexte(new UnSort().afficheToi());
        }
        if (leChoix == "Cr�er une race") {
            afficheEnContexte(new UneRace().creeToi());
        }
        if (leChoix == "Cr�er une capacit�") {
            afficheEnContexte(new UneCapacite().afficheToi());
        }
        if ((leChoix == "Editer un personnage") || (leChoix == "Editer des stats")) {
            Vector<UneCreature> lesCreatures = saCarte.getsesCreatures();
            String[] lesChoixPossibles = new String[lesCreatures.size()];
            for (int i = 0; i < lesCreatures.size(); i++) {
                lesChoixPossibles[i] = lesCreatures.get(i).getsonNom();
            }
            try {
                String leChoixCrea = (String) JOptionPane.showInputDialog(null, "Quel personnage modifie-t-on?", "Edition Personnage", JOptionPane.QUESTION_MESSAGE, null, lesChoixPossibles, lesChoixPossibles[0]);
                if (leChoixCrea != null) {
                    int i = 0;
                    while (!leChoixCrea.equals(lesChoixPossibles[i])) i++;
                    if (leChoix == "Editer un personnage") afficheEnContexte(lesCreatures.get(i).afficheToi()); else afficheEnContexte(lesCreatures.get(i).editeTesStats());
                }
            } catch (Exception lException) {
                JOptionPane.showMessageDialog(null, "Il n'y a aucun personnage sur la carte.", "Edition Personnage", JOptionPane.WARNING_MESSAGE);
            }
        }
        if (leChoix == "Editer un objet simple") {
            String choix = JOptionPane.showInputDialog(null, "Un objet nomm�?", "Editer objet", JOptionPane.QUESTION_MESSAGE);
            if (choix != null) afficheEnContexte(new UnObjet(choix).afficheToi());
        }
        if (leChoix == "Editer une armure") {
            String choix = JOptionPane.showInputDialog(null, "Un armure nomm�?", "Editer objet", JOptionPane.QUESTION_MESSAGE);
            if (choix != null) try {
                afficheEnContexte(new UneArmure(choix).afficheToi());
            } catch (Exception lException) {
                JOptionPane.showMessageDialog(null, "Ceci n'est pas une armure.", "Erreur", JOptionPane.WARNING_MESSAGE);
            }
        }
        if (leChoix == "Editer une arme") {
            String choix = JOptionPane.showInputDialog(null, "Un arme nomm�?", "Editer objet", JOptionPane.QUESTION_MESSAGE);
            if (choix != null) try {
                afficheEnContexte(new UneArme(choix).afficheToi());
            } catch (Exception lException) {
                JOptionPane.showMessageDialog(null, "Ceci n'est pas une arme", "Erreur", JOptionPane.WARNING_MESSAGE);
            }
        }
        if (leChoix == "Editer un consommable") {
            String choix = JOptionPane.showInputDialog(null, "Un consommable nomm�?", "Editer objet", JOptionPane.QUESTION_MESSAGE);
            if (choix != null) try {
                afficheEnContexte(new UnConsommable(choix).afficheToi());
            } catch (Exception lException) {
                JOptionPane.showMessageDialog(null, "Ceci n'est pas un consommable", "Erreur", JOptionPane.WARNING_MESSAGE);
            }
        }
        if (leChoix == "Poser un objet simple") {
            String laChaine = JOptionPane.showInputDialog(null, "Quel objet pose-t-on et o�?\nDonnez les informations au format suivant : nom,X,Y", "Poser un objet", JOptionPane.QUESTION_MESSAGE);
            if (laChaine != null) try {
                int X = Integer.parseInt(laChaine.split(",")[1]);
                int Y = Integer.parseInt(laChaine.split(",")[2]);
                saCarte.getsesCases()[Y][X].addUnObjet(new UnObjet(laChaine.split(",")[0]));
                afficheLaCarte();
            } catch (Exception lException) {
                JOptionPane.showMessageDialog(null, "Format de donn�e non reconnu ou case inexistante", "ERREUR", JOptionPane.ERROR_MESSAGE);
            }
        }
        if (leChoix == "Poser une armure") {
            String laChaine = JOptionPane.showInputDialog(null, "Quelle armure pose-t-on et o�?\nDonnez les informations au format suivant : nom,X,Y", "Poser un objet", JOptionPane.QUESTION_MESSAGE);
            if (laChaine != null) try {
                int X = Integer.parseInt(laChaine.split(",")[1]);
                int Y = Integer.parseInt(laChaine.split(",")[2]);
                saCarte.getsesCases()[Y][X].addUnObjet(new UneArmure(laChaine.split(",")[0]));
                afficheLaCarte();
            } catch (Exception lException) {
                JOptionPane.showMessageDialog(null, "Format de donn�e non reconnu ou case inexistante", "ERREUR", JOptionPane.ERROR_MESSAGE);
            }
        }
        if (leChoix == "Poser une arme") {
            String laChaine = JOptionPane.showInputDialog(null, "Quelle arme pose-t-on et o�?\nDonnez les informations au format suivant : nom,X,Y", "Poser un objet", JOptionPane.QUESTION_MESSAGE);
            if (laChaine != null) try {
                int X = Integer.parseInt(laChaine.split(",")[1]);
                int Y = Integer.parseInt(laChaine.split(",")[2]);
                saCarte.getsesCases()[Y][X].addUnObjet(new UneArme(laChaine.split(",")[0]));
                afficheLaCarte();
            } catch (Exception lException) {
                JOptionPane.showMessageDialog(null, "Format de donn�e non reconnu ou case inexistante", "ERREUR", JOptionPane.ERROR_MESSAGE);
            }
        }
        if (leChoix == "Poser un consommable") {
            String laChaine = JOptionPane.showInputDialog(null, "Quel consommable pose-t-on et o�?\nDonnez les informations au format suivant : nom,X,Y", "Poser un objet", JOptionPane.QUESTION_MESSAGE);
            if (laChaine != null) try {
                int X = Integer.parseInt(laChaine.split(",")[1]);
                int Y = Integer.parseInt(laChaine.split(",")[2]);
                saCarte.getsesCases()[Y][X].addUnObjet(new UnConsommable(laChaine.split(",")[0]));
                afficheLaCarte();
            } catch (Exception lException) {
                JOptionPane.showMessageDialog(null, "Format de donn�e non reconnu ou case inexistante", "ERREUR", JOptionPane.ERROR_MESSAGE);
            }
        }
        if (leChoix == "D�placer un personnage") {
            Vector<UnJoueur> lesJoueurs = saCarte.getsesJoueurs();
            String[] lesChoixPossibles = new String[lesJoueurs.size() + 1];
            lesChoixPossibles[0] = "Tous les joueurs";
            for (int i = 1; i <= lesJoueurs.size(); i++) {
                lesChoixPossibles[i] = lesJoueurs.get(i - 1).getsonNom();
            }
            try {
                String leChoixCrea = (String) JOptionPane.showInputDialog(this, "Qui d�place-t-on?", "Deplacement Personnage", JOptionPane.QUESTION_MESSAGE, null, lesChoixPossibles, lesChoixPossibles[0]);
                if (leChoixCrea != null) {
                    String laDest = JOptionPane.showInputDialog(this, "Veuillez indiquer la destination de " + leChoixCrea + ".\nUtilisez le format suivant : direction,distance.\nDirections possibles : N,S,E,O,NE,NO,SE,SO.\nLa distance est en case (rappel : 1 case = 1,5m)", "Deplacement", JOptionPane.QUESTION_MESSAGE);
                    if (leChoixCrea.equals(lesChoixPossibles[0])) {
                        sonMode = ETAT_INTER;
                        for (int i = 0; i < lesJoueurs.size(); i++) {
                            deplaceLePerso(lesJoueurs.get(i), laDest);
                        }
                        sonMode = JOUER;
                        afficheLaCarte();
                    } else {
                        int i = 1;
                        while (!leChoixCrea.equals(lesChoixPossibles[i])) i++;
                        deplaceLePerso(lesJoueurs.get(i - 1), laDest);
                    }
                }
            } catch (Exception lException) {
                JOptionPane.showMessageDialog(this, "Il n'y a aucun joueur sur la carte.", "Edition Personnage", JOptionPane.WARNING_MESSAGE);
            }
        }
        if (leChoix == "Lancer des d�s") {
            String lesDes = JOptionPane.showInputDialog(this, "Vous �tes sur le point de lancer des d�s.\nUtilisez la syntaxe suivante : xdy, o� x est le nombre de d�s, et y leur valeur.", "Lancer de d�s", JOptionPane.QUESTION_MESSAGE);
            if (lesDes != null) {
                if (lesDes.split("[dD]").length != 2) JOptionPane.showMessageDialog(null, "Erreur : vous n'avez pas respect� le format.", "Erreur : mauvaise saisie!", JOptionPane.ERROR_MESSAGE); else {
                    try {
                        int[] leResultat = lanceLesDes(lesDes);
                        int total = 0;
                        String laChaine = "R�sultat : \n";
                        for (int i = 0; i < leResultat.length; i++) {
                            laChaine += "D� " + (i + 1) + " : " + leResultat[i] + "\n";
                            total += leResultat[i];
                        }
                        laChaine += "total : " + total;
                        JOptionPane.showMessageDialog(null, laChaine);
                    } catch (Exception lException) {
                        JOptionPane.showMessageDialog(null, "Erreur : vous n'avez pas respect� le format.", "Erreur : mauvaise saisie!", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
        if (leChoix == "des �v�nements") {
            Vector<UnEvenement> lesEvenements = saCarte.getsesEvenements();
            String laChaine = "<HTML>Liste des �v�nements : <br>";
            if (lesEvenements.size() == 0) laChaine += "Il n'y a pas d'�v�nements sur la carte"; else for (int i = 0; i < lesEvenements.size(); i++) {
                laChaine += lesEvenements.get(i).sonNom + " (" + lesEvenements.get(i).saCase.getsonX() + "," + lesEvenements.get(i).saCase.getsonY() + ")<br>";
            }
            laChaine += "</HTML>";
            JPanel lePanneau = new JPanel();
            lePanneau.add(new JLabel(laChaine));
            afficheEnContexte(lePanneau);
        }
        if (leChoix == "des personnages") {
            Vector<UneCreature> lesCreatures = saCarte.getsesCreatures();
            String laChaine = "<HTML>Liste des personnages : <br>";
            if (lesCreatures.size() == 0) laChaine += "Il n'y a pas de personnages sur la carte"; else for (int i = 0; i < lesCreatures.size(); i++) {
                laChaine += lesCreatures.get(i).getsonNom() + " (" + lesCreatures.get(i).getsaPosX() + "," + lesCreatures.get(i).getsaPosY() + ")<br>";
            }
            laChaine += "</HTML>";
            JPanel lePanneau = new JPanel();
            lePanneau.add(new JLabel(laChaine));
            afficheEnContexte(lePanneau);
        }
        if (leChoix == "A propos...") JOptionPane.showMessageDialog(this, "<html><table border='0'><tr><td><img src='http://wankin.net/perso_coffre_evt_exemple.jpg' width='60' height='60'></td><th><div align='left'><p>L'&eacute;diteur Donjons et Dragons - version beta 0.7<br>Copyright &copy; 2007, Aur&eacute;lien P&ecirc;cheur, Jonathan Mondon, Yannick Balla<br>L'&eacute;diteur Donjons et Dragons est un logiciel d'aide &agrave; la gestion de campagne pour le jeu &quot;Donjons et Dragons&quot;.<br>Merci &agrave; Jean-Philippe Farrugia l'ensemble du corps enseignant de l'IUT A - Lyon 1 pour leur aide et formation.</p></div></th></tr></table><p><br>This program is free software: you can redistribute it and/or modify it under the terms of the GNU General<br>Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option)<br>any later version.</p><p><br>This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without <br>even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the<br>GNU General Public License for more details.</p><p><br>You should have received a copy of the GNU General Public License along with this program. <br></p><p>If not, see <a href='http://www.gnu.org/licenses/'>http://www.gnu.org/licenses/</a>.</p></html>", "A propos...", JOptionPane.PLAIN_MESSAGE);
        if (leChoix == "Aide") JOptionPane.showMessageDialog(this, "Vous trouverez les fichiers d'aide et tutoriaux dans le r�pertoire \"Aide\" l� o� vous avez install� l'Editeur Donjon et Dragon.", "A propos...", JOptionPane.INFORMATION_MESSAGE);
        if (leChoix == "Modifier la description") saCarte.changeSaDescription();
    }
