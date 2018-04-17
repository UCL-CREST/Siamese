    private boolean sendVCalendarEventEmail(String fromEmail, String fromName, String toEmail, String cc, String bcc, String subject, String toName, String emailBody) {
        try {
            Session session = null;
            try {
                Context ctx = (Context) new InitialContext().lookup("java:comp/env");
                session = (javax.mail.Session) ctx.lookup("mail/MailSession");
            } catch (NamingException e1) {
                Logger.error(this, e1.getMessage(), e1);
            }
            if (session == null) {
                Logger.debug(this, "No Mail Session Available.");
                return false;
            }
            Logger.debug(this, "Delivering mail using: " + session.getProperty("mail.smtp.host") + " as server.");
            MimeMessage message = new MimeMessage(session);
            Multipart mp = new MimeMultipart();
            if ((fromEmail != null) && (fromName != null) && (0 < fromEmail.trim().length())) {
                message.setFrom(new InternetAddress(fromEmail, fromName));
            } else if ((fromEmail != null) && (0 < fromEmail.trim().length())) {
                message.setFrom(new InternetAddress(fromEmail));
            }
            if (toName != null) {
                String[] recipients = toEmail.split("[;,]");
                for (String recipient : recipients) {
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient, toName));
                }
            } else {
                String[] recipients = toEmail.split("[;,]");
                for (String recipient : recipients) {
                    message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
                }
            }
            if (UtilMethods.isSet(cc)) {
                String[] recipients = cc.split("[;,]");
                for (String recipient : recipients) {
                    message.addRecipient(Message.RecipientType.CC, new InternetAddress(recipient));
                }
            }
            if (UtilMethods.isSet(bcc)) {
                String[] recipients = bcc.split("[;,]");
                for (String recipient : recipients) {
                    message.addRecipient(Message.RecipientType.BCC, new InternetAddress(recipient));
                }
            }
            message.setSubject(subject);
            MimeBodyPart meetingPart = new MimeBodyPart();
            meetingPart.setDataHandler(new DataHandler(new StringDataSource(emailBody, "text/calendar", "")));
            mp.addBodyPart(meetingPart, 0);
            message.setContent(mp);
            Transport.send(message);
        } catch (Exception e) {
            Logger.warn(this, e.toString());
            return false;
        }
        return true;
    }
