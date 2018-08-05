    public static void sendmail(String to, String subject, String body) throws IOException, AddressException, MessagingException {
        if (useNotifications) {
            String from = "Rapid Task Manager <rtm@rapidtaskmanager.com>";
            String host = hostIP;
            Properties props = new Properties();
            props.put("mail.smtp.host", host);
            props.put("mail.debug", "true");
            Session session = Session.getInstance(props);
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            InternetAddress[] address = { new InternetAddress(to) };
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject(subject);
            msg.setSentDate(new Date());
            msg.setText(body);
            Transport.send(msg);
        }
    }
