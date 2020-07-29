    public static void lireInfo(Scanner scan) {
        StringTokenizer text;
        String ligne = "";
        String id = "";
        while (scan.hasNextLine()) {
            int nb = 3;
            ligne = scan.nextLine();
            text = new StringTokenizer(ligne, ",");
            while (text.hasMoreTokens() && nb == 3) {
                if (text.countTokens() == 2) {
                    id = text.nextToken();
                    for (PointGPS point : DonneesGPS.getL_Points()) {
                        if (point.getNom().equals(id)) {
                            new Croisement(point, "", text.nextToken());
                        }
                    }
                    nb--;
                } else {
                    id = text.nextToken();
                    for (PointGPS point : DonneesGPS.getL_Points()) {
                        if (point.getNom().equals(id)) {
                            new Croisement(point, text.nextToken(), text.nextToken());
                        }
                    }
                    nb--;
                }
            }
        }
    }
