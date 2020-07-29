    public boolean send() {
        try {
            Properties props = System.getProperties();
            if (!mailhost.equals("")) {
                props.put("mail.smtp.host", mailhost);
            }
            Session session = Session.getDefaultInstance(props, null);
            MimeMessage m = new MimeMessage(session);
            m.setFrom(new InternetAddress(from));
            InternetAddress[] adr_to = new InternetAddress[1];
            adr_to[0] = new InternetAddress(to);
            m.setRecipients(Message.RecipientType.TO, adr_to);
            if ((bcc != null) && (bcc.length() > 0)) {
                InternetAddress[] adr_bcc = new InternetAddress[1];
                adr_bcc[0] = new InternetAddress(bcc);
                m.setRecipients(Message.RecipientType.BCC, adr_bcc);
            }
            m.setSubject(subject);
            m.setSentDate(new Date());
            m.setContent(msg, "text/plain");
            Transport.send(m);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
