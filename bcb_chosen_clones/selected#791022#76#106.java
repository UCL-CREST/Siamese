    public static void send(String to, String subject, String body) throws Exception {
        Properties props = null;
        try {
            props = getConfig();
        } catch (ConfigurationException e) {
            throw e;
        }
        final String username = props.getProperty(USER_PROPERTY);
        final String password = props.getProperty(PASSWORD_PROPERTY);
        Authenticator authenticator = null;
        if (!StringUtils.isEmpty(password)) {
            authenticator = new javax.mail.Authenticator() {

                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            };
        } else {
            Log.warn("CouLn't find credentials using properties '{}' and '{}'", USER_PROPERTY, PASSWORD_PROPERTY);
        }
        Session session = Session.getDefaultInstance(props, authenticator);
        InternetAddress[] destinationAddress = InternetAddress.parse(to, false);
        Message msg = new MimeMessage(session);
        msg.setRecipients(Message.RecipientType.TO, destinationAddress);
        msg.setSubject(subject);
        msg.setDataHandler(new DataHandler(new ByteArrayDataSource(body, "text/plain")));
        msg.setHeader("X-Mailer", "JerkBotEmailer");
        msg.setSentDate(new Date());
        Transport.send(msg);
    }
