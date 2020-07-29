    @SuppressWarnings("unused")
    private boolean copyAvecProgressNIO(File sRC2, File dEST2, JProgressBar progressEnCours) {
        boolean resultat = false;
        long PourcentEnCours = 0;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(sRC2);
        } catch (FileNotFoundException e) {
            try {
                Historique.ecrire("Erreur � la copie du fichier " + sRC2 + " pour la raison suivante : " + e);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return true;
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(dEST2);
        } catch (FileNotFoundException e) {
            try {
                Historique.ecrire("Erreur � la creation du fichier " + dEST2 + " pour la raison suivante : " + e);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return true;
        }
        java.nio.channels.FileChannel channelSrc = fis.getChannel();
        java.nio.channels.FileChannel channelDest = fos.getChannel();
        progressEnCours.setValue(0);
        progressEnCours.setString(sRC2 + " : 0 %");
        try {
            long tailleCopie = channelSrc.transferTo(0, channelSrc.size(), channelDest);
        } catch (IOException e) {
            try {
                Historique.ecrire("Erreur � la copie du fichier " + sRC2 + " vers la destination " + dEST2 + " pour la raison suivante : " + e);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return true;
        }
        progressEnCours.setValue(100);
        progressEnCours.setString(sRC2 + " : 100 %");
        try {
            if (channelSrc.size() == channelDest.size()) {
                resultat = true;
            } else {
                resultat = false;
            }
        } catch (IOException e) {
            try {
                Historique.ecrire("Erreur � la copie du fichier " + sRC2 + " pour la raison suivante : " + e);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return true;
        }
        try {
            fis.close();
        } catch (IOException e) {
            try {
                Historique.ecrire("Impossible de fermer le flux � la copie du fichier " + sRC2 + " pour la raison suivante : " + e);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return true;
        }
        try {
            fos.close();
        } catch (IOException e) {
            try {
                Historique.ecrire("Impossible de fermer le flux � la copie du fichier " + dEST2 + " pour la raison suivante : " + e);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return true;
        }
        return (resultat);
    }
