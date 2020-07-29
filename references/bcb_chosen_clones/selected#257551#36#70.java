    public static boolean postMail(String[] recipients, String subject, String message) {
        if (recipients.length == 0) throw new IllegalArgumentException("É necessário informar pelo menos um destinatário.");
        final Properties authProps = new Properties();
        try {
            authProps.load(new FileInputStream(conPropsFile));
        } catch (Exception e1) {
            e1.printStackTrace();
            return false;
        }
        Authenticator auth = new Authenticator() {

            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(authProps.get(account).toString(), authProps.get(password).toString());
            }
        };
        Session session = Session.getInstance(authProps, auth);
        session.setDebug(true);
        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(authProps.get(account).toString()));
            InternetAddress[] addrs = new InternetAddress[recipients.length];
            for (int i = 0; i < recipients.length; i++) {
                addrs[i] = new InternetAddress(recipients[i]);
            }
            msg.addRecipients(Message.RecipientType.BCC, addrs);
            msg.setSentDate(new Date());
            msg.setSubject(subject);
            msg.setContent(message, "text/html");
            Transport.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
