    public void onReceiveEvent(ReceiveEvent event) {
        String subject;
        String body;
        String cid = null;
        Date now = new Date();
        subject = "Facsimile received";
        if ((event.getCidName() != null) && (!"".equals(event.getCidName()))) {
            cid = event.getCidName();
        } else if ((event.getCidNumber() != null) && (!"".equals(event.getCidNumber()))) {
            cid = event.getCidNumber();
        }
        try {
            File f = new File(event.getFilename());
            if (!f.exists()) {
                f = new File("log" + File.separator + "c" + event.getCommunicationIdentifier());
                subject = "Facsimile failed";
                body = "A facsimile failed to be received at " + now + "\n\n" + "See the attached log file for session details.\n";
            } else {
                body = "The attached facsimile was received " + now + "\n";
            }
            if ((event.getMessage() != null) && (!"".equals(event.getMessage()))) {
                body += "The server's message is:\n\n\t" + event.getMessage();
            }
            if (cid != null) subject += " from " + cid;
            Session s = Session.getDefaultInstance(properties);
            MimeMessage msg = new MimeMessage(s);
            msg.addRecipients(Message.RecipientType.TO, properties.getProperty(KEY_TO));
            msg.setSubject(subject);
            msg.addHeader("From", properties.getProperty(KEY_FROM));
            msg.addHeader("Date", rfc822df.format(now));
            msg.addHeader("X-MailListener", "$Id: MailListener.java 162 2009-03-26 21:42:09Z sjardine $");
            MimeBodyPart part0 = new MimeBodyPart();
            part0.setText(body);
            FileDataSource fds = new FileDataSource(f);
            fds.setFileTypeMap(new MimetypesFileTypeMap());
            DataHandler fdh = new DataHandler(fds);
            MimeBodyPart part1 = new MimeBodyPart();
            part1.setDataHandler(fdh);
            part1.setDisposition(Part.INLINE);
            part1.setFileName(f.getName());
            MimeMultipart mp = new MimeMultipart();
            mp.addBodyPart(part0);
            mp.addBodyPart(part1);
            msg.setContent(mp);
            Transport.send(msg);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
