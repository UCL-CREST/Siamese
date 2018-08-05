    public void sendMailMessage(String subject, String text, String attachment) {
        Properties props = new Properties();
        props.setProperty("mail.smtp.host", host);
        props.setProperty("mail.smtp.port", port);
        if (debug) {
            props.put("mail.debug", new Boolean(debug));
        }
        Session session = Session.getInstance(props, null);
        session.setDebug(debug);
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            InternetAddress[] address = { new InternetAddress(to) };
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(subject);
            msg.setSentDate(new Date());
            if (attachment == null) {
                msg.setDataHandler(new DataHandler(text, mimeType));
            } else {
                MimeBodyPart msgBodyPart = new MimeBodyPart();
                Multipart multipart = new MimeMultipart();
                msgBodyPart.setDataHandler(new DataHandler(text, mimeType));
                multipart.addBodyPart(msgBodyPart);
                msgBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(attachment);
                msgBodyPart.setDataHandler(new DataHandler(source));
                msgBodyPart.setFileName(new File(attachment).getName());
                multipart.addBodyPart(msgBodyPart);
                msg.setContent(multipart);
            }
            Transport.send(msg);
        } catch (MessagingException e) {
            Application.getLogger().error("Mail: Unable to send message.", e);
        }
    }
