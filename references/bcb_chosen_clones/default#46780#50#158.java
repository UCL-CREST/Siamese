    public void run() {
        try {
            DataStore store = DataStore.getInstance();
            String to = store.getProperty("email.to");
            String from = store.getProperty("email.from");
            String server = store.getProperty("email.server.address");
            String port = store.getProperty("email.server.port");
            String auth = store.getProperty("email.auth.enabled");
            String user = store.getProperty("email.auth.user");
            String password = store.getProperty("email.auth.password");
            String security = store.getProperty("email.security");
            logString("Email Thread Started (" + subject + ")");
            if (server == null || server.trim().length() == 0) {
                logString("Email sending failed, (server) is not set correctly!");
                return;
            }
            if (port == null || port.trim().length() == 0) {
                logString("Email sending failed, (port) is not set correctly!");
                return;
            }
            if (to == null || to.trim().length() == 0) {
                logString("Email sending failed, (to) is not set correctly!");
                return;
            }
            StringTokenizer tokenizer = new StringTokenizer(to, ",");
            Vector<InternetAddress> toAddresses = new Vector<InternetAddress>();
            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken();
                if (token != null && token.trim().length() > 0) {
                    token = token.trim();
                    logString("TO: " + token);
                    try {
                        toAddresses.add(new InternetAddress(token));
                    } catch (Exception e) {
                        logString("Invalid To Address (" + token + ")");
                        return;
                    }
                }
            }
            if (toAddresses.size() == 0) {
                logString("No Valid to addresses");
                return;
            }
            if (from == null || from.trim().length() == 0) {
                logString("Email sending failed, (from) is not set correctly!");
                return;
            }
            InternetAddress fromAddress = null;
            try {
                fromAddress = new InternetAddress(from);
            } catch (Exception e) {
                logString("Invalid From Address (" + from + ")");
                return;
            }
            if ("1".equals(auth)) {
                if (user == null || user.trim().length() == 0) {
                    logString("Email sending failed, (user) is not set correctly!");
                    return;
                }
                if (password == null || password.trim().length() == 0) {
                    logString("Email sending failed, (password) is not set correctly!");
                    return;
                }
            }
            Properties props = new Properties();
            props.put("mail.smtp.connectiontimeout", "30000");
            props.put("mail.smtp.timeout", "60000");
            logString("server address: " + server);
            logString("server port: " + port);
            props.put("mail.smtp.host", server);
            props.put("mail.smtp.port", port);
            if ("1".equals(auth)) {
                logString("Using Authentication");
                props.put("mail.smtp.user", user);
                props.put("mail.smtp.auth", "true");
            }
            if ("1".equals(security)) {
                logString("Using security type 1 (STARTTLS)");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.socketFactory.port", port);
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.socketFactory.fallback", "false");
            }
            props.put("mail.smtp.debug", "true");
            Session session = null;
            if ("1".equals(auth)) {
                session = Session.getInstance(props, new SMTPAuthenticator(user, password));
            } else {
                session = Session.getInstance(props);
            }
            session.setDebug(true);
            MimeMessage msg = new MimeMessage(session);
            msg.setText(body);
            msg.setSubject(subject);
            msg.setFrom(fromAddress);
            for (int x = 0; x < toAddresses.size(); x++) {
                msg.addRecipient(Message.RecipientType.TO, toAddresses.get(x));
            }
            logString("Starting mail transport");
            Transport.send(msg);
            logString("Email Sent");
        } catch (Exception e) {
            logString("Error Sending Email (" + e.getMessage() + ")");
            e.printStackTrace();
        } finally {
            logString("Email Thread Exiting (" + subject + ")");
            finished = true;
        }
    }
