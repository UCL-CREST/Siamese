    @Override
    public void sendMail() {
        try {
            Session session = Session.getInstance(props);
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            InternetAddress[] addresses = getAddresses(this.to);
            msg.setRecipients(Message.RecipientType.TO, addresses);
            InternetAddress[] ccAddresses = getAddresses(this.cc);
            msg.setRecipients(Message.RecipientType.CC, ccAddresses);
            InternetAddress[] bccAddresses = getAddresses(this.bcc);
            msg.setRecipients(Message.RecipientType.BCC, bccAddresses);
            msg.setSubject(this.getTitle());
            BodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent(this.getMessage() + this.getFooter(), "text/html");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(bodyPart);
            if (this.getDataSource() != null) {
                bodyPart = new MimeBodyPart();
                bodyPart.setDataHandler(new DataHandler(this.getDataSource()));
                bodyPart.setFileName(this.getDataSource().getName());
                multipart.addBodyPart(bodyPart);
            }
            msg.setContent(multipart);
            Transport.send(msg);
        } catch (AddressException e) {
            logger.error("Address is wrong", e);
        } catch (MessagingException e) {
            logger.error("Error while sending the mail", e);
        }
    }
