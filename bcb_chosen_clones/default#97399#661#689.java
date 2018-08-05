    public void sendIntroPackage() {
        newRandomPasswd();
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "localhost");
            Session mailsession = Session.getDefaultInstance(props, null);
            Message email = new MimeMessage(mailsession);
            email.setFrom(new InternetAddress("ozymandias-admin@aetherial.net"));
            InternetAddress[] address = { new InternetAddress(userEmail) };
            email.setRecipients(Message.RecipientType.TO, address);
            email.setSubject("Ozymandias Registration");
            String content = new String();
            content = content + "Welcome to the Ozymandias Internet Mailing List Archive!\n\n";
            content = content + "Your account has been successfully created and you may log on and personalize\n";
            content = content + "the service at (http://fire.aetherial.net:8081/ozymandias/login).\n\n";
            content = content + "Your password has been initialized to " + userPasswd + ". Please change this password\n";
            content = content + "upon logging in.\n\n";
            content = content + "If you have any questions about the service, please check the online documentation\n";
            content = content + "at (http://fire.aetherial.net:8081/ozymandias/help). If you have any questions\n";
            content = content + "about the Terms of Service, it can be viewed at\n";
            content = content + "(http://fire.aetherial.net:8081/ozymandias/signup?action=tos)\n\n";
            content = content + "Once more, thank you for using this product for your mailing list needs.\n\n";
            content = content + "-Ozymandias Staff";
            email.setText(content);
            Transport.send(email);
        } catch (MessagingException e) {
            System.out.println(e);
        }
    }
