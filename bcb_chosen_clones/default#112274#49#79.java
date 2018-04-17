    public boolean render(Vector shapes) throws ImageRenderException {
        String tempFile = renderToTempFile(shapes);
        Session session;
        MimeMessage msg;
        try {
            session = Session.getDefaultInstance(prop, null);
            msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(prop.getProperty("mail.from", "gh@mbl.is")));
            msg.addRecipient(RecipientType.TO, new InternetAddress(address));
            msg.setSubject(prop.getProperty("mail.subject", "Whiteboard image"));
            Multipart multipart = new MimeMultipart();
            BodyPart msgBody = new MimeBodyPart();
            msgBody.setText(prop.getProperty("mail.body", "This message contains your whiteboard image"));
            multipart.addBodyPart(msgBody);
            BodyPart msgImg = new MimeBodyPart();
            DataSource source = new FileDataSource(tempFile);
            msgImg.setDataHandler(new DataHandler(source));
            msgImg.setFileName("whiteboard.png");
            multipart.addBodyPart(msgImg);
            msg.setContent(multipart);
        } catch (MessagingException e) {
            throw new ImageRenderException("Couldn't initialize mail message: " + e);
        }
        try {
            Transport.send(msg);
        } catch (MessagingException e) {
            throw new ImageRenderException("Couldn't send message: " + e);
        }
        new File(tempFile).delete();
        return true;
    }
