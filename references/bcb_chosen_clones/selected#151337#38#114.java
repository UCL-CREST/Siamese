    public void sendMail(String messageText, String messageSubject) {
        if (messageText == null || messageSubject == null) {
            throw new RuntimeException("Both the text of the email and a subject are required");
        }
        String userAgent = request.getHeader("user-agent");
        String userIPAddress = request.getRemoteAddr();
        String userHost = request.getRemoteHost();
        messageText += "\n--- Additional Info ---\n" + "User Browser:  " + userAgent + "\n" + "User IP Addr:  " + userIPAddress + "\n" + "User Hostname: " + userHost + "\n";
        String mailHost = servletConfig.getServletContext().getInitParameter("mailHost");
        String mailTo = servletConfig.getServletContext().getInitParameter("mailTo");
        String mailFrom = servletConfig.getServletContext().getInitParameter("mailFrom");
        String mailUser = servletConfig.getServletContext().getInitParameter("mailUser");
        String mailPassword = servletConfig.getServletContext().getInitParameter("mailPassword");
        String mailTls = servletConfig.getServletContext().getInitParameter("mailTls");
        String mailSsl = servletConfig.getServletContext().getInitParameter("mailSsl");
        String mailPort = servletConfig.getServletContext().getInitParameter("mailPort");
        String mailDebug = servletConfig.getServletContext().getInitParameter("mailDebug");
        if (mailHost == null || mailTo == null || mailFrom == null) {
            throw new RuntimeException("Unable to get mail init parameters");
        }
        Properties mailProperties = System.getProperties();
        mailProperties.put("mail.transport.protocol", "smtp");
        mailProperties.put("mail.smtp.host", mailHost);
        if (mailPort != null && mailPort.equals("25") == false) {
            mailProperties.put("mail.smtp.port", mailPort);
        } else {
            mailProperties.put("mail.smtp.port", "25");
        }
        if (mailSsl != null && mailSsl.equals("yes")) {
            mailProperties.put("mail.smtp.ssl.enable", "true");
            mailProperties.put("mail.smtp.ssl.trust", "*");
            if (mailPort != null) {
                mailProperties.put("mail.smtp.socketFactory.port", mailPort);
            } else {
                mailProperties.put("mail.smtp.socketFactory.port", "465");
            }
            mailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            mailProperties.put("mail.smtp.socketFactory.fallback", "false");
        } else {
            mailProperties.put("mail.smtp.ssl.enable", "false");
        }
        if (mailDebug != null && mailDebug.equals("yes")) {
            mailProperties.put("mail.debug", "true");
        } else {
            mailProperties.put("mail.debug", "false");
        }
        if (mailUser != null && mailUser.equals("")) {
            mailProperties.put("mail.smtp.auth", "true");
        } else {
            mailProperties.put("mail.smtp.auth", "false");
        }
        if (mailTls != null && mailTls.equals("yes")) {
            mailProperties.put("mail.smtp.starttls.enable", "true");
        } else {
            mailProperties.put("mail.smtp.starttls.enable", "false");
        }
        Session mailSession;
        if (mailUser != null && mailUser.equals("")) {
            mailSession = Session.getInstance(mailProperties, new SMTPAuthenticator(mailUser, mailPassword));
        } else {
            mailSession = Session.getInstance(mailProperties, null);
        }
        try {
            Message mailMessage = new MimeMessage(mailSession);
            mailMessage.setFrom(new InternetAddress(mailFrom));
            mailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailTo, false));
            mailMessage.setSubject(messageSubject);
            mailMessage.setText(messageText);
            mailMessage.setHeader("X-Mailer", "JavaMail");
            mailMessage.setSentDate(new Date());
            Transport.send(mailMessage);
        } catch (javax.mail.internet.AddressException ex) {
            throw new RuntimeException("Unable to prepare mail message", ex);
        } catch (javax.mail.MessagingException ex) {
            throw new RuntimeException("Unable to send mail message:" + ex.toString());
        }
    }
