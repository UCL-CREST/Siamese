    public boolean sendMail(String mailurl, String filePath, String callerID, Contact contact, String smtpHost, String smtpUserAccount) {
        logger.debug("MailSender::sendMail - attempting delivery at: " + mailurl);
        boolean bSUCCESS = false;
        boolean isSMTP = false;
        try {
            Session s = null;
            if (mailurl.toUpperCase().startsWith("POP")) {
                isSMTP = true;
                Properties props = new Properties();
                props.put("mail.smtp.host", smtpHost);
                s = Session.getInstance(props);
            } else {
                s = Session.getInstance(new Properties());
            }
            MimeMessage message = new MimeMessage(s);
            message.setFrom(new InternetAddress(contact.getEmail(), "voicemail"));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(contact.getEmail(), contact.getEmail()));
            if (callerID == null) callerID = "";
            message.addHeader("x-caller-id", callerID);
            message.addHeader("x-message-path", filePath);
            String subject = "Voicemail Message";
            if (!"".equals(callerID) && !"unknown".equalsIgnoreCase(callerID)) subject += " From " + callerID;
            message.setSubject(subject);
            Multipart multipart = new MimeMultipart();
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("voicemail message is attached...");
            multipart.addBodyPart(messageBodyPart);
            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(filePath);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName("message.wav");
            multipart.addBodyPart(messageBodyPart);
            messageBodyPart.setDisposition("CRITICAL");
            message.setContent(multipart);
            message.setSentDate(new Date());
            if (isSMTP) {
                Transport trans = s.getTransport("smtp");
                message.setFrom(new InternetAddress(smtpUserAccount, "voicemail"));
                try {
                    trans.connect();
                    Transport.send(message);
                } catch (Exception e) {
                    logger.debug("MailSender::sendMail() - failed to send via SMTP: " + e.getMessage());
                } finally {
                    trans.close();
                }
            } else {
                MailConnection mail = null;
                ;
                try {
                    mail = new MailConnection(mailurl);
                    Folder inbox = mail.getInbox();
                    inbox.appendMessages(new Message[] { (Message) message });
                    bSUCCESS = true;
                } catch (Exception e) {
                    logger.debug("MailSender::sendMail() - failed to append message: " + e.getMessage());
                } finally {
                    mail.closeInbox();
                }
            }
            logger.debug("MailSender::sendMail() - SUCCESSFULLY DELIVERED!");
        } catch (javax.mail.MessagingException me) {
            logger.error("MailSender::sendMail() - MessagingException: " + me.getMessage());
        } catch (Exception e) {
            logger.error("MailSender::sendMail() - Exception: " + e.getMessage());
        }
        return bSUCCESS;
    }
