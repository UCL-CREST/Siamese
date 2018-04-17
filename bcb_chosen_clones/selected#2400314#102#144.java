    public static void sendSimpleHTMLMessage(Map<String, String> recipients, String object, String htmlContent, String from) {
        String message;
        try {
            File webinfDir = ClasspathUtils.getClassesDir().getParentFile();
            File mailDir = new File(webinfDir, "mail");
            File templateFile = new File(mailDir, "HtmlMessageTemplate.html");
            StringWriter sw = new StringWriter();
            Reader r = new BufferedReader(new FileReader(templateFile));
            IOUtils.copy(r, sw);
            sw.close();
            message = sw.getBuffer().toString();
            message = message.replaceAll("%MESSAGE_HTML%", htmlContent).replaceAll("%APPLICATION_URL%", FGDSpringUtils.getExternalServerURL());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Properties prop = getRealSMTPServerProperties();
        if (prop != null) {
            try {
                MimeMultipart multipart = new MimeMultipart("related");
                BodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setContent(message, "text/html");
                multipart.addBodyPart(messageBodyPart);
                sendHTML(recipients, object, multipart, from);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        } else {
            StringBuffer contenuCourriel = new StringBuffer();
            for (Entry<String, String> recipient : recipients.entrySet()) {
                if (recipient.getValue() == null) {
                    contenuCourriel.append("À : " + recipient.getKey());
                } else {
                    contenuCourriel.append("À : " + recipient.getValue() + "<" + recipient.getKey() + ">");
                }
                contenuCourriel.append("\n");
            }
            contenuCourriel.append("Sujet : " + object);
            contenuCourriel.append("\n");
            contenuCourriel.append("Message : ");
            contenuCourriel.append("\n");
            contenuCourriel.append(message);
        }
    }
