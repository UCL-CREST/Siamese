    public static void sendMail(String mailServer, String from, String to, String subject, String mensagem) throws MessagingException {
        Properties mailProps = new Properties();
        mailProps.put("mail.smtp.host", mailServer);
        mailProps.put("mail.smtp.auth", "true");
        Session mailSession = Session.getDefaultInstance(mailProps, new Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("sistema.adrs", "ufcquixada");
            }
        });
        String texto = mensagem;
        texto = texto.replaceAll("\n", "\r\n");
        mailSession.setDebug(true);
        mailProps.put("mail.debug", "true");
        mailProps.put("mail.smtp.debug", "true");
        mailProps.put("mail.mime.charset", "ISO-8859-1");
        mailProps.put("mail.smtp.port", "465");
        mailProps.put("mail.smtp.starttls.enable", "true");
        mailProps.put("mail.smtp.socketFactory.port", "465");
        mailProps.put("mail.smtp.socketFactory.fallback", "false");
        mailProps.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        Message message = new MimeMessage(mailSession);
        message.setFrom(new InternetAddress(from));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSentDate(new Date());
        message.setSubject(subject);
        message.setText(mensagem);
        Transport.send(message);
    }
