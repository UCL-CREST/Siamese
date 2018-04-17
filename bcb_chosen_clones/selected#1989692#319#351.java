    protected boolean sendMail(String toList, String subject, String message, boolean important) throws CruiseControlException {
        boolean emailSent = false;
        if (toList != null && toList.trim().length() != 0) {
            LOG.debug("Sending email to: " + toList);
            Session session = Session.getDefaultInstance(getMailProperties(), null);
            session.setDebug(LOG.isDebugEnabled());
            try {
                Message msg = new MimeMessage(session);
                msg.setFrom(getFromAddress());
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toList, false));
                msg.setSubject(subject);
                msg.setSentDate(new Date());
                String importance = (important) ? "High" : "Normal";
                msg.addHeader("Importance", importance);
                addContentToMessage(message, msg);
                if (userName != null && password != null) {
                    msg.saveChanges();
                    Transport transport = session.getTransport("smtp");
                    transport.connect(mailHost, userName, password);
                    transport.sendMessage(msg, msg.getAllRecipients());
                    transport.close();
                } else {
                    Transport.send(msg);
                }
                emailSent = true;
            } catch (SendFailedException e) {
                LOG.warn(e.getMessage(), e);
            } catch (MessagingException e) {
                throw new CruiseControlException(e.getClass().getName() + ": " + e.getMessage(), e);
            }
        }
        return emailSent;
    }
