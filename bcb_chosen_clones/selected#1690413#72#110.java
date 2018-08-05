    public static boolean sendHtmlMail(MailSenderInfo mailInfo, List fileList) {
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
            String toAddress = mailInfo.getToAddress();
            toAddress = decorativeToAddress(toAddress);
            InternetAddress[] toList = new InternetAddress().parse(toAddress);
            mailMessage.setRecipients(javax.mail.Message.RecipientType.TO, toList);
            mailMessage.setSubject(mailInfo.getSubject());
            mailMessage.setSentDate(new java.util.Date());
            Multipart mainPart = new MimeMultipart();
            BodyPart html = new MimeBodyPart();
            try {
                if (fileList.size() > 0) {
                    FileItem fileItem = (FileItem) fileList.get(0);
                    MimeBodyPart attachPart1 = createAttachment(fileItem);
                    mainPart.addBodyPart(attachPart1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            html.setContent(mainPart);
            html.setText(mailInfo.getContent());
            mainPart.addBodyPart(html);
            mailMessage.setContent(mainPart);
            Transport.send(mailMessage);
            return true;
        } catch (MessagingException ex) {
            ex.printStackTrace();
            return false;
        }
    }
