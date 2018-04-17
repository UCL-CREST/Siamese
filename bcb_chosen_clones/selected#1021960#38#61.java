    public void enviaEmail(String smtpHost, String from, String to, String assunto, String conteudo, AnexoTO[] arquivos, String assinatura) throws Exception {
        Properties props = System.getProperties();
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", "25");
        props.put("mail.smtp.auth", "false");
        props.put("mail.mime.charset", "ISO-8859-1");
        Session session = Session.getDefaultInstance(props, null);
        session.setDebug(true);
        MimeMessage message = new MimeMessage(session);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss z Z");
        message.addHeader("date-time", sdf.format(new Date()));
        message.setContentLanguage(new String[] { "pr_BR" });
        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(assunto, "ISO-8859-1");
        Multipart multipart = new MimeMultipart();
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.addHeader("Content-Type: text/html;", "charset=\"iso-8859-1\"");
        messageBodyPart.setContent(conteudo, "text/html");
        multipart.addBodyPart(messageBodyPart);
        DataSource source;
        message.setContent(multipart);
        Transport.send(message);
    }
