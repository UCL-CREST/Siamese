    private boolean sendMail(HashMap glossary, String hostName) {
        try {
            String from = CofaxUtil.getString(glossary, "msgFrom");
            String to = CofaxUtil.getString(glossary, "msgTo");
            String subject = CofaxUtil.getString(glossary, "msgSubject");
            StringBuffer text = new StringBuffer();
            Session smtpSession;
            Properties props = new Properties();
            props.put("mail.smtp.host", hostName);
            smtpSession = Session.getDefaultInstance(props, null);
            text.append("This Story has been sent to you by : " + from);
            text.append("<pre>\n");
            text.append(CofaxUtil.getString(glossary, "msgBody"));
            text.append("\n");
            text.append("</pre><br>\n");
            text.append("<p>");
            text.append(CofaxUtil.getString(glossary, "msgPromotion"));
            text.append("\n");
            MimeMessage message = new MimeMessage(smtpSession);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setContent(text.toString(), "text/html");
            Transport.send(message);
        } catch (Exception e) {
            this.errorMsg = "An error occured while trying to send your message!";
            this.errorMsg = this.errorMsg + " Please be sure the to and from addresses are valid.";
            return false;
        }
        return true;
    }
