    public boolean enviaNotificacion(String app, String det) throws Exception {
        StringBuffer sb = new StringBuffer("");
        StringTokenizer st = new StringTokenizer(this.config.getProperty("to"), ",");
        sb.append(this.sdf.format(new Date())).append("\n\n").append("La aplicación '").append(app).append("' ha presentado un error.\n\nDetalles del error:\n").append(det);
        Session session = Session.getInstance(this.parseConfig());
        session.setDebug(this.debug);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(this.config.getProperty("from")));
        while (st.hasMoreTokens()) {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(st.nextToken()));
        }
        message.setSubject(this.titulo);
        message.setContent(sb.toString(), "text/plain");
        Transport.send(message);
        return true;
    }
