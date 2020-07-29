    private void sendMail() throws Exception {
        String to = getControllerSetting("systemoperatormail").trim();
        String from = getControllerSetting("mailfromaddress");
        String host = getControllerSetting("smtphost");
        String filename = System.getProperty("user.home") + "/" + ".hardtokenmgmt0_0.log";
        String subject = getControllerSetting("systemoperatorsubject");
        Properties props = System.getProperties();
        props.put("mail.smtp.host", host);
        if (getControllerSetting("smtpusername") != null) {
            props.put("mail.smtp.user", getControllerSetting("smtpusername"));
            props.put("mail.smtp.auth", "true");
        }
        Session session = Session.getInstance(props, null);
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(from));
        InternetAddress[] address = { new InternetAddress(to) };
        msg.setRecipients(Message.RecipientType.TO, address);
        msg.setSubject(subject);
        MimeBodyPart mbp1 = new MimeBodyPart();
        mbp1.setText(message);
        Multipart mp = new MimeMultipart();
        mp.addBodyPart(mbp1);
        File attachFile = new File(filename);
        if (attachFile.exists()) {
            MimeBodyPart mbp2 = new MimeBodyPart();
            FileDataSource fds = new FileDataSource(filename);
            mbp2.setDataHandler(new DataHandler(fds));
            mbp2.setFileName(fds.getName());
            mp.addBodyPart(mbp2);
        }
        msg.setContent(mp);
        msg.setSentDate(new Date());
        if (getControllerSetting("smtpusername") == null) {
            Transport.send(msg);
        } else {
            Transport t = session.getTransport();
            try {
                t.connect(host, getControllerSetting("smtpusername"), getControllerSetting("smtppassword"));
                t.sendMessage(msg, msg.getAllRecipients());
            } finally {
                t.close();
            }
        }
    }
