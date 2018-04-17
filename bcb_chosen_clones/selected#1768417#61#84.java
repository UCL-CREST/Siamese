    public void sendMessage(EntityManager em, User to, String subject, String messageText, List<TaskAttachment> attachemnts) throws MessagingException {
        String toEmail = to.getEmail();
        if (!to.isNotifyByEmail() || to.getEmail() == null) return;
        Message message = new MimeMessage(getAdminSession());
        message.setFrom(getAdminAddress());
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
        message.setSubject(subject);
        Multipart multipart = new MimeMultipart();
        BodyPart part = new MimeBodyPart();
        part.setText(messageText);
        multipart.addBodyPart(part);
        if (attachemnts != null) {
            for (TaskAttachment attachment : attachemnts) {
                BinaryData data = em.find(BinaryData.class, attachment.getBinaryId());
                BodyPart attPart = new MimeBodyPart();
                DataSource source = new ByteArrayDataSource(data.getContent(), "application/octet-stream ");
                attPart.setDataHandler(new DataHandler(source));
                attPart.setFileName(attachment.getName());
                multipart.addBodyPart(attPart);
            }
        }
        message.setContent(multipart);
        Transport.send(message);
    }
