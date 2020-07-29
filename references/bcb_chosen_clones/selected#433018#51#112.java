    public static void save(String from, String recipient, InputStream in, MimeMessage message) throws IOException, MessagingException, DocumentVideException {
        ConversationManager conversationManager = FGDSpringUtils.getConversationManager();
        conversationManager.beginConversation();
        FGDDelegate delegate = new FGDDelegate();
        UtilisateurIFGD utilisateur = delegate.getUtilisateurParCourriel(from);
        if (utilisateur == null) {
            String responseEmailSubject = "Votre adresse ne correspond pas à celle d'un utilisateur d'IntelliGID";
            String responseEmailMessage = "<h3>Pour sauvegarder un courriel, vous devez être un utilisateur d'IntelliGID et l'adresse de courrier électronique utilisée doit être celle apparaissant dans votre profil.</h3>";
            String sender = recipient.endsWith("localhost") ? FGDSpringUtils.getExpediteurSupport() : recipient;
            Map<String, String> recipients = new HashMap<String, String>();
            recipients.put(from, null);
            MailUtils.sendSimpleHTMLMessage(recipients, responseEmailSubject, responseEmailMessage, sender);
            return;
        }
        File tempFile = File.createTempFile("email", ".eml");
        tempFile.deleteOnExit();
        BufferedInputStream bis = new BufferedInputStream(in);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(tempFile));
        IOUtils.copy(bis, bos);
        IOUtils.closeQuietly(bis);
        IOUtils.closeQuietly(bos);
        if (message == null) {
            GestionnaireProprietesMimeMessageParser gestionnaire = new GestionnaireProprietesMimeMessageParser();
            message = gestionnaire.asMimeMessage(new BufferedInputStream(new FileInputStream(tempFile)));
        }
        String subject;
        try {
            subject = message.getSubject().replace("Fwd:", "").trim();
        } catch (MessagingException e) {
            subject = "Message sans sujet";
        }
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
        File emailFile = new File(tempDir, FilenameUtils.normalize(subject) + ".eml");
        FileUtils.copyFile(tempFile, emailFile);
        FicheDocument ficheDocument = new FicheDocument();
        ficheDocument.setFicheCompletee(false);
        ficheDocument.setDateCreationHorodatee(new Date());
        ficheDocument.setUtilisateurSoumetteur(utilisateur);
        ficheDocument.getLangues().addAll(getLanguesDefaut());
        ficheDocument.setCourriel(true);
        FileIOContenuFichierElectronique contenuFichier = new FileIOContenuFichierElectronique(emailFile, "multipart/alternative");
        SupportDocument support = new SupportDocument();
        support.setFicheDocument(ficheDocument);
        FichierElectroniqueUtils.setContenu(ficheDocument, support, contenuFichier, utilisateur);
        ficheDocument.setTitre(subject);
        delegate.sauvegarder(ficheDocument, utilisateur);
        String modifyEmail = "http://" + FGDSpringUtils.getServerHost() + ":" + FGDSpringUtils.getServerPort() + "/" + FGDSpringUtils.getApplicationName() + "/app/modifierDocument/id/" + ficheDocument.getId();
        System.out.println(modifyEmail);
        String responseEmailSubject = "Veuillez compléter la fiche du courriel «" + subject + "»";
        String responseEmailMessage = "<h3>Le courrier électronique a été sauvegardé, mais il est nécessaire de <a href=\"" + modifyEmail + "\">compléter sa fiche</a>.</h3>";
        String sender = recipient.endsWith("localhost") ? FGDSpringUtils.getExpediteurSupport() : recipient;
        try {
            MailUtils.sendSimpleHTMLMessage(utilisateur, responseEmailSubject, responseEmailMessage, sender);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        conversationManager.commitTransaction();
        tempFile.delete();
    }
