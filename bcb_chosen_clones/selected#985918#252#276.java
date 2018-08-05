    private static void copier(FichierElectronique source, FichierElectronique cible) throws IOException {
        cible.setNom(source.getNom());
        cible.setTaille(source.getTaille());
        cible.setTypeMime(source.getTypeMime());
        cible.setSoumetteur(source.getSoumetteur());
        cible.setDateDerniereModification(source.getDateDerniereModification());
        cible.setEmprunteur(source.getEmprunteur());
        cible.setDateEmprunt(source.getDateEmprunt());
        cible.setNumeroVersion(source.getNumeroVersion());
        InputStream inputStream = source.getInputStream();
        OutputStream outputStream = cible.getOutputStream();
        try {
            IOUtils.copy(inputStream, outputStream);
        } finally {
            try {
                inputStream.close();
            } finally {
                outputStream.close();
            }
            if (source instanceof FichierElectroniqueDefaut) {
                FichierElectroniqueDefaut fichierElectroniqueTemporaire = (FichierElectroniqueDefaut) source;
                fichierElectroniqueTemporaire.deleteTemp();
            }
        }
    }
