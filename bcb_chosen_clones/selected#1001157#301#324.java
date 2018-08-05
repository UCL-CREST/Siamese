    private boolean execSend(String address, String title, String body) throws Exception {
        Properties props = new Properties();
        props.put("mail.smtp.host", emailHost);
        props.put("mail.smtp.auth", "true");
        Session session = Session.getDefaultInstance(props, new Authenticator() {

            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAccount, emailPassword);
            }
        });
        MimeMessage message = new MimeMessage(session);
        message.setSubject(title);
        message.setFrom(new InternetAddress(emailAccount));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(address));
        message.setSentDate(new Date());
        Multipart mp = new MimeMultipart("related");
        BodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(body, "text/html;charset=utf-8");
        mp.addBodyPart(bodyPart);
        message.setContent(mp);
        Transport.send(message);
        logger.info("向邮件地址:{}发送邮件成功！", address);
        return true;
    }
