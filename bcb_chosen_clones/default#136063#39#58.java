    public static void lireInfo(Scanner fichierCSV) {
        StringTokenizer text;
        String ligne = "";
        String id = "";
        while (fichierCSV.hasNextLine()) {
            int nb = 3;
            ligne = fichierCSV.nextLine();
            text = new StringTokenizer(ligne, ",");
            while (text.hasMoreTokens() && nb == 3) {
                id = text.nextToken();
                for (Vecteur vecteur : DonneesGPS.getL_Vecteurs()) {
                    if (vecteur.getId().equals(id)) {
                        vecteur.setNom(text.nextToken());
                        vecteur.setVitesse(Integer.parseInt(text.nextToken()));
                    }
                }
                nb--;
            }
        }
    }
