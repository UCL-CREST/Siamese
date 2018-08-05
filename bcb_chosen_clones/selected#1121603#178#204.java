    public boolean uploadFile(String localFilePath, String subject) {
        boolean result = false;
        if (session == null) {
            session = Session.getInstance(GMailCfg.GMAIL_SMTP_PROP, new Authenticator() {

                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(name + "@gmail.com", pwd);
                }
            });
        }
        try {
            Message msg = new MimeMessage(session);
            msg.setHeader("X-Mailer", "smtpsend");
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(name + "@gmail.com", false));
            msg.setSubject(subject);
            MimeBodyPart mbp = new MimeBodyPart();
            mbp.attachFile(localFilePath);
            MimeMultipart mmp = new MimeMultipart();
            mmp.addBodyPart(mbp);
            msg.setContent(mmp);
            Transport.send(msg);
        } catch (Exception e) {
            Logger.log("use smtp send mail fail!");
            e.printStackTrace();
        }
        return result;
    }
