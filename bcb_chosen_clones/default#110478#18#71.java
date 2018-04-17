    private boolean sendEmail(Image img, String text) {
        Properties props = new Properties();
        String gateway = localConfig.getGwAddress();
        props.put("mail.smtp.host", gateway);
        Session session = null;
        if (localConfig.getUsername() != null) {
            props.put("mail.smtp.auth", "true");
            Authenticator auth = new SMTPAuthenticator(localConfig.getUsername(), localConfig.getPassword());
            session = Session.getDefaultInstance(props, auth);
        } else session = Session.getDefaultInstance(props, null);
        session.setDebug(false);
        Message msg = new MimeMessage(session);
        File tmpfile = null;
        try {
            InternetAddress addressFrom = new InternetAddress(defaultSender);
            msg.setFrom(addressFrom);
            InternetAddress[] addressTo = new InternetAddress[1];
            String destination = localConfig.getEmailAddress();
            addressTo[0] = new InternetAddress(destination);
            msg.setRecipients(Message.RecipientType.TO, addressTo);
            msg.setSubject(defaultSubject);
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(text);
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            if (img != null) {
                messageBodyPart = new MimeBodyPart();
                tmpfile = File.createTempFile(defaultPrefix, ".jpg");
                ImageIO.write((BufferedImage) (img), "jpg", tmpfile);
                DataSource source = new FileDataSource(tmpfile);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(tmpfile.getName());
                multipart.addBodyPart(messageBodyPart);
            }
            msg.setContent(multipart);
            Transport.send(msg);
            tmpfile.delete();
        } catch (AddressException ae) {
            System.err.println("Destination address is malformed!");
            ae.printStackTrace();
            if (tmpfile != null) tmpfile.delete();
            return false;
        } catch (MessagingException me) {
            System.err.println("Error sending email");
            me.printStackTrace();
            if (tmpfile != null) tmpfile.delete();
            return false;
        } catch (IOException ioe) {
            System.err.println("Error managing image");
            ioe.printStackTrace();
            return false;
        }
        return true;
    }
