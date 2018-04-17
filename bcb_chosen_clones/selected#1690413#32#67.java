    public boolean sendTextMail(MailSenderInfo mailInfo, List fileList) throws Exception {
        MyAuthenticator authenticator = null;
        Properties pro = mailInfo.getProperties();
        if (mailInfo.isValidate()) {
            authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
        }
        Session sendMailSession = Session.getDefaultInstance(pro, authenticator);
        try {
            javax.mail.Message mailMessage = new MimeMessage(sendMailSession);
            Address from = new InternetAddress(mailInfo.getFromAddress());
            mailMessage.setFrom(from);
            Address to = new InternetAddress(mailInfo.getToAddress());
            mailMessage.setRecipient(javax.mail.Message.RecipientType.TO, to);
            mailMessage.setSubject(mailInfo.getSubject());
            mailMessage.setSentDate(new java.util.Date());
            String mailContent = mailInfo.getContent();
            logger.debug("mailContent is: " + mailContent);
            mailMessage.setText(mailContent);
            Multipart mainPart = new MimeMultipart();
            try {
                if (fileList.size() > 0) {
                    FileItem fileItem = (FileItem) fileList.get(0);
                    MimeBodyPart attachPart1 = createAttachment(fileItem);
                    mainPart.addBodyPart(attachPart1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            mailMessage.setContent(mainPart);
            Transport.send(mailMessage);
            return true;
        } catch (MessagingException ex) {
            ex.printStackTrace();
            throw new Exception(ex);
        }
    }
