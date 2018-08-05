    public static void setContenu(ContenuFichierElectronique contenuFichier, FichierElectronique fichierElectronique, UtilisateurIFGD utilisateurCourant) throws IOException, DocumentVideException {
        if (contenuFichier != null) {
            SupportDocument support = fichierElectronique.getSupport();
            support.setFichierElectronique(fichierElectronique);
            FicheDocument ficheDocument = support.getFicheDocument();
            String nomFichier = contenuFichier.getNomFichier();
            String extension = FilenameUtils.getExtension(nomFichier);
            if (ficheDocument.getFichierElectronique(nomFichier) != null) {
                FichierElectronique fichierElectroniqueExistant = ficheDocument.getFichierElectronique(nomFichier);
                if (fichierElectroniqueExistant.getId() != null && !fichierElectroniqueExistant.getId().equals(fichierElectronique.getId())) {
                    throw new FichierElectroniqueExistantException(nomFichier, ficheDocument);
                }
            }
            if (fichierElectronique.getId() == null) {
                if (OfficeDocumentPropertiesUtil.canWriteIdIGID(extension)) {
                    Long idIgid = OfficeDocumentPropertiesUtil.getIdIGID(contenuFichier);
                    if (idIgid != null) {
                        throw new FichierElectroniqueExistantException(nomFichier, idIgid, ficheDocument);
                    }
                }
            }
            InputStream inputStream = contenuFichier.getInputStream();
            OutputStream outputStream = fichierElectronique.getOutputStream();
            try {
                IOUtils.copy(inputStream, outputStream);
            } finally {
                try {
                    inputStream.close();
                } finally {
                    outputStream.close();
                }
            }
            String typeMime = contenuFichier.getContentType();
            long tailleFichier = contenuFichier.getTailleFichier();
            Date dateDerniereModification = new Date();
            fichierElectronique.setNom(nomFichier);
            fichierElectronique.setTypeMime(extension);
            creerFormatSiNecessaire(typeMime, extension);
            fichierElectronique.setTaille(tailleFichier);
            fichierElectronique.setDateDerniereModification(dateDerniereModification);
            fichierElectronique.setSoumetteur(utilisateurCourant);
            if (extension.endsWith("msg")) {
                CourrielUtils.peuplerMetadonneesCourriel(fichierElectronique.getNom(), ficheDocument, contenuFichier.getInputStream(), utilisateurCourant);
            } else if (extension.endsWith("eml")) {
                Map<String, Object> properties = new GestionnaireProprietesMimeMessageParser().parseMsg(contenuFichier.getInputStream());
                CourrielUtils.peuplerMetadonneesCourriel(fichierElectronique.getNom(), ficheDocument, properties, utilisateurCourant);
            } else {
                FGDProprietesDocumentUtils.copierMetadonneesProprietes(fichierElectronique, ficheDocument);
            }
        }
    }
