    @Override
    void lireFichier() {
        try {
            String ligneCSV = "";
            this.file = new File(nom);
            this.scan = new Scanner(file);
            if (this.scan.hasNextLine()) ligneCSV += this.scan.nextLine();
            StringTokenizer firstLigne = new StringTokenizer(ligneCSV, "\n");
            int resultat = 0;
            if (firstLigne.hasMoreTokens()) resultat = EnteteCSV.entete(firstLigne.nextToken());
            if (resultat == 1) {
                FormatCSVCarteNueLecteur.lireInfo(scan);
            } else if (resultat == 2) {
                FormatCSVRouteLecteur.lireInfo(scan);
            } else if (resultat == 3) {
                FormatCSVCroisementLecteur.lireInfo(scan);
            } else System.out.println("Mauvaise entete de fichier");
            this.scan.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
