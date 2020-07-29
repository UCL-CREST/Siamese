    private void envoyerMail(String message_dest, String label, String nom) {
        String message_objet = "Fin de l'execution " + label;
        String message_corps = "Bonjour,\n\n" + "L'exécution " + label + " créée par " + nom + " s'est terminée.\n\n" + "-----------------------------------------------------------------------------\n" + "Ce message a été généré automatiquement. Veuillez ne pas y répondre, merci.\n" + "La Team Pelias III";
        Message mesg;
        Properties props = new Properties();
        props.put("mail.smtp.host", "mailhost.insa-rennes.fr");
        Session session;
        session = Session.getDefaultInstance(props, null);
        session.setDebug(true);
        try {
            mesg = new MimeMessage(session);
            mesg.setFrom(new InternetAddress("pelias@insa-rennes.fr"));
            InternetAddress toAddress = new InternetAddress(message_dest);
            mesg.addRecipient(Message.RecipientType.TO, toAddress);
            mesg.setSubject(message_objet);
            mesg.setText(message_corps);
            Transport.send(mesg);
        } catch (MessagingException ex) {
            while ((ex = (MessagingException) ex.getNextException()) != null) {
                ex.printStackTrace();
            }
        }
    }
