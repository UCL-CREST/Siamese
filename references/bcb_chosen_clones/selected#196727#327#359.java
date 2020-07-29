    public void sendEmail(Event event) throws Exception {
        if (log.isDebugEnabled()) {
            log.info("Starting send mail...");
        }
        StringBuilder eMailBody = new StringBuilder();
        User user = userMgr.get(event.getUserID().toString());
        Slot slot = slotMgr.get(event.getSlotID().toString());
        try {
            eMailBody.append("Hello, " + user.getFirstName() + " from the Winter War Convention!\n\r\n\r");
            eMailBody.append("This e-mail is being sent to inform you that the event \"" + event.getEventName() + "\" has been updated and/or approved for the " + slot.getSlotName() + " time slot!\n\r\n\r");
            eMailBody.append("If you have any questions, comments, or need to make changes to your event, please contact the convention chairman, Don McKinney! \n\r");
            eMailBody.append("Thank you! \n\r");
            Properties props = new Properties();
            props.put("mail.smtp.host", "winterwar.org");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.user", "registrar@mail.winterwar.org");
            props.put("mail.smtp.pass", "Battle0n");
            Session mailSession = Session.getDefaultInstance(props, new WWAuthenticator());
            Message msg = new MimeMessage(mailSession);
            InternetAddress addressFrom = new InternetAddress("registrar@winterwar.org");
            msg.setFrom(addressFrom);
            InternetAddress[] addressTo = new InternetAddress[1];
            addressTo[0] = new InternetAddress(user.getEmail());
            msg.setRecipients(Message.RecipientType.TO, addressTo);
            msg.addHeader("MyHeaderName", "myHeaderValue");
            msg.setSubject("Winter War Convention Account Information");
            msg.setContent(eMailBody.toString(), "text/plain");
            Transport.send(msg);
        } catch (Exception ex) {
            log.error(ex);
            for (StackTraceElement element : ex.getStackTrace()) log.error(element.toString());
        }
    }
