    public SendEmail(String to, String pass, UserDetailsBean userInfo) {
        Properties props = new Properties();
        props.put("mail.smtp.user", d_email);
        props.put("mail.smtp.host", d_host);
        props.put("mail.smtp.port", d_port);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", d_port);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        @SuppressWarnings("unused") SecurityManager security = System.getSecurityManager();
        try {
            Authenticator auth = new SMTPAuthenticator();
            Session session = Session.getInstance(props, auth);
            MimeMessage msg = new MimeMessage(session);
            m_text = "Dear " + userInfo.getFirstName() + " " + userInfo.getLastName() + "\n\n" + "Here is the new password for your account at netSalesForum. " + "\nOnce you login using this password;" + "go to your profile and change the password if this one is difficult to remember.\n\n" + "New password: " + pass + " \n\n" + "Click here to open the netsalesforum home page: " + loginPageLink + "\n\n netSalesForum automated system.";
            msg.setText(m_text);
            msg.setSubject(m_subject);
            msg.setFrom(new InternetAddress(d_email));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            Transport.send(msg);
        } catch (Exception mex) {
            mex.printStackTrace();
        }
    }
