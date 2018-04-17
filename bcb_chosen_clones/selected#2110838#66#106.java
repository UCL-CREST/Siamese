    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new URL(urlInfo).openStream()));
            String ligneEnCours;
            int i = 0;
            informations = "";
            while ((ligneEnCours = in.readLine()) != null) {
                switch(i) {
                    case 0:
                        version = ligneEnCours;
                        break;
                    case 1:
                        url = ligneEnCours;
                        break;
                    default:
                        informations += ligneEnCours + '\n';
                        break;
                }
                i++;
            }
            in.close();
            erreur = false;
        } catch (IOException e) {
            erreur = true;
            texteErreur = e.getMessage();
            if (texteErreur.equals("Network is unreachable")) {
                texteErreur = "Pas de réseau";
                numErreur = 1;
            }
            if (e instanceof FileNotFoundException) {
                texteErreur = "Problème paramétrage";
                numErreur = 2;
            }
            e.printStackTrace();
        } finally {
            for (ActionListener al : listeners) {
                al.actionPerformed(null);
            }
        }
    }
