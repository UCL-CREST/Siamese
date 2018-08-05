    public static void sendPlainEmail(String smtpHost, String smtpUsername, String smtpPassword, boolean smtpAuthRequired, String receiverAddress, String receiverName, String sendersAddress, String sendersName, String sub, String msg) throws CatalogException {
        if (smtpHost == null) {
            String errMsg = "Could not send email: smtp host address is null";
            logger.error(errMsg);
            throw new CatalogException(errMsg);
        }
        try {
            Properties props = System.getProperties();
            props.put("mail.smtp.host", smtpHost);
            props.put("mail.smtp.auth", Boolean.toString(smtpAuthRequired));
            props.put("mail.smtp.debug", "true");
            Authenticator auth = null;
            if (smtpAuthRequired) {
                auth = new SMTPAuthenticator(smtpUsername, smtpPassword);
            }
            Session session = Session.getDefaultInstance(props, auth);
            MimeMessage message = new MimeMessage(session);
            message.addHeader("Content-type", "text/plain");
            message.setSubject(sub);
            message.setFrom(new InternetAddress(sendersAddress, sendersName));
            message.addRecipients(Message.RecipientType.TO, receiverAddress);
            message.setText(msg);
            message.setSentDate(new Date());
            Transport.send(message);
        } catch (Exception e) {
            String errorMsg = "Could not send email";
            logger.error(errorMsg, e);
            throw new CatalogException("errorMsg", e);
        }
    }
