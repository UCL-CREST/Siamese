    public void send(EMail anEMailMessage) {
        String Sender = anEMailMessage.getSenderAddress();
        String Subject = anEMailMessage.getSubject();
        String Text = anEMailMessage.getMessage();
        String Recipient = anEMailMessage.getRecipientAddress();
        try {
            Properties Props = new Properties();
            Props.put("mail.smtp.host", Server);
            Session aSession = Session.getDefaultInstance(Props, null);
            Message Msg = new MimeMessage(aSession);
            InternetAddress From = new InternetAddress(Sender);
            Msg.setFrom(From);
            InternetAddress[] Recipients = { new InternetAddress(Recipient) };
            Msg.setRecipients(Message.RecipientType.TO, Recipients);
            Msg.setSubject(Subject);
            Msg.setContent(Text, "text/html");
            Transport.send(Msg);
        } catch (MessagingException MsgEx) {
            logEvent(ERROR, "Error during sending of EMail", MsgEx.toString());
            MsgEx.printStackTrace();
        } catch (Exception E) {
            logEvent(ERROR, "Error during sending of EMail", E.toString());
        }
    }
