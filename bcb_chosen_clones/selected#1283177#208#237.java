    public void postMailAttachements(String recipients[], String subject, String message, String from, String filename) {
        try {
            Session session = getSession();
            if (from == null) from = DEFAULT_FROM;
            Message msg = new MimeMessage(session);
            InternetAddress addressFrom = new InternetAddress(from);
            msg.setFrom(addressFrom);
            InternetAddress[] addressTo = new InternetAddress[recipients.length];
            for (int i = 0; i < recipients.length; i++) {
                addressTo[i] = new InternetAddress(recipients[i]);
            }
            msg.setRecipients(Message.RecipientType.TO, addressTo);
            msg.setSubject(subject);
            MimeBodyPart mbp1 = new MimeBodyPart();
            mbp1.setText(message);
            MimeBodyPart mbp2 = new MimeBodyPart();
            FileDataSource fds = new FileDataSource(filename);
            mbp2.setDataHandler(new DataHandler(fds));
            mbp2.setFileName(fds.getName());
            Multipart mp = new MimeMultipart();
            mp.addBodyPart(mbp1);
            mp.addBodyPart(mbp2);
            msg.setContent(mp);
            msg.setSentDate(new Date());
            Transport.send(msg);
        } catch (MessagingException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, TC.get("SendMail.UnableToSendEmail"), TC.get("SendMail.ErrorSendingEmail"), JOptionPane.ERROR_MESSAGE);
        }
    }
