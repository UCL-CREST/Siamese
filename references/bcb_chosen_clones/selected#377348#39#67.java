    public boolean enviar(MensajeEmail me, Envio envio, List destinatarios) throws Exception {
        try {
            InitialContext jndiContext = new InitialContext();
            Session mailSession = (Session) jndiContext.lookup("java:/" + envio.getCuenta().getEmail());
            MimeMessage msg = new MimeMessage(mailSession);
            InternetAddress[] direcciones = getDirecciones(destinatarios);
            msg.setRecipients(javax.mail.Message.RecipientType.BCC, direcciones);
            msg.setSubject(me.getTitulo());
            byte[] mensaje = me.getMensaje();
            String contenido;
            if (me.isHtml()) {
                contenido = new String(mensaje, ConstantesXML.ENCODING);
            } else {
                contenido = StringEscapeUtils.escapeHtml(new String(mensaje, ConstantesXML.ENCODING));
            }
            msg.setContent(contenido, "text/html");
            msg.setHeader("X-Mailer", "JavaMailer");
            msg.setSentDate(new java.util.Date());
            log.debug("Vamos a enviar Email: " + msg.getContent());
            try {
                Transport.send(msg);
            } catch (Exception ex) {
                throw ex;
            }
            return true;
        } catch (Exception ex) {
            throw ex;
        }
    }
