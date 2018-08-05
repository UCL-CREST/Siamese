    public void spedisciMail() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "mail.papiniweb.it");
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");
        Session session = Session.getInstance(props, new MyAuth());
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("info@papinicomputer.it"));
            InternetAddress[] address = { new InternetAddress("info@papiniweb.it") };
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject("");
            msg.setSentDate(new Date());
            msg.setText("Message body string");
            msg.setSubject("Invio messaggio");
            Transport.send(msg);
        } catch (MessagingException e) {
            JOptionPane.showMessageDialog(null, e.toString(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
