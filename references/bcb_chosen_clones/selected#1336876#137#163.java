    void mailForm(String mailHost, String senderAddress, String subject, String message, String emailRecipient) {
        if (status == 0) {
            Properties props = System.getProperties();
            props.put("mail.smtp.host", mailHost);
            Session emailsession = Session.getDefaultInstance(props, null);
            pending_failure = pending_failure_start + emailMailForm_Mode_failure;
            pending_failure += emailMailFormStart_Task;
            try {
                Message email = new MimeMessage(emailsession);
                email.setFrom(new InternetAddress(senderAddress));
                InternetAddress[] address = { new InternetAddress(emailRecipient) };
                email.setRecipients(Message.RecipientType.TO, address);
                email.setSubject(subject);
                email.setSentDate(new Date());
                email.setHeader("X-Mailer", "MailFormJava");
                email.setText(message);
                pending_failure = pending_failure_start + emailMailForm_Mode_failure;
                pending_failure += emailMailFormBeforeSend_Task;
                Transport.send(email);
            } catch (MessagingException e) {
                (cm.logger()).println(1, "XCEmailCommand::mailForm  Exception: " + e.getMessage());
                (cm.logger()).println(1, "XCEmailCommand::mailForm  Mail may not have been sent to " + emailRecipient);
                errorDescription = "e.getMessage() Mail may not have been sent to " + emailRecipient;
                status = pending_failure;
            }
        }
    }
