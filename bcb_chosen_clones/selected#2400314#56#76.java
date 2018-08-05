    public static boolean sendMail(String destinataire, String contenu, String sujet, String from) {
        Properties prop = getRealSMTPServerProperties();
        if (prop != null) {
            try {
                Session session = Session.getDefaultInstance(prop, null);
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(FGDSpringUtils.getExpediteurSupport()));
                InternetAddress[] internetAddresses = { new InternetAddress(destinataire) };
                message.setRecipients(Message.RecipientType.TO, internetAddresses);
                message.setContent(contenu, "text/html");
                message.setSubject(sujet);
                message.setHeader("X-Mailer", MAILER_VERSION);
                Transport.send(message);
                return true;
            } catch (AddressException e) {
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
