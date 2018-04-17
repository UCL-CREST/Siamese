    @SuppressWarnings("rawtypes")
    public String enviar() {
        String error = null;
        if (asunto == null) asunto = "(no subject)";
        if (mensaje == null) mensaje = new StringBuffer("");
        try {
            Properties props = new Properties();
            boolean doAuth = !"none".equals(auth);
            boolean useTLS = "tls".equals(auth);
            Session session;
            props.put("mail.from", from);
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", port);
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.debug", debug);
            if (useTLS) {
                props.put("mail.smtp.socketFactory.port", port);
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.socketFactory.fallback", "false");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.ssl", "true");
            }
            if (doAuth) {
                Authenticator authenticator = new ar.com.AmberSoft.util.Authenticator(user, password);
                props.put("mail.user", user);
                props.put("mail.smtp.auth", "true");
                session = Session.getInstance(props, authenticator);
            } else session = Session.getInstance(props);
            MimeMessage message = new MimeMessage(session);
            for (Iterator iterator = destinatarios.iterator(); iterator.hasNext(); ) {
                String addressee = (String) iterator.next();
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(addressee));
            }
            message.setFrom(new InternetAddress(from));
            message.setText(mensaje.toString());
            message.setSubject(asunto);
            Transport.send(message);
        } catch (Throwable e) {
            logger.error(Tools.getStackTrace(e));
        }
        return error;
    }
