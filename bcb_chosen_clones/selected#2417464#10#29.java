    public static void main(java.lang.String[] args) {
        try {
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", "mail.lineadecodigo.com");
            Autentificacion pwd = new Autentificacion();
            Session mailSession = Session.getInstance(props, pwd);
            mailSession.setDebug(true);
            Message msg = new MimeMessage(mailSession);
            msg.setFrom(new InternetAddress("yo@lineadecodigo.com"));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse("destinatario@email.com"));
            msg.setSubject("Tema del mensaje");
            String msgBody = "Cuerpo del mensaje";
            DataHandler dh = new DataHandler(msgBody, "text/plain");
            msg.setDataHandler(dh);
            javax.mail.Transport.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
