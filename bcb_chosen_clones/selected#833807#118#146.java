    private void sendMessage(String token) {
        XMPPService xmpp = XMPPServiceFactory.getXMPPService();
        JID jid = new JID(ADMIN_JID);
        String msgBody = "Recieved an unkown price code please enter a value for this code \n\r" + "http://twitterinmail.appspot.com/decode?id=" + token;
        Message msg = new MessageBuilder().withRecipientJids(jid).withBody(msgBody).build();
        boolean messageSent = false;
        if (xmpp.getPresence(jid).isAvailable()) {
            SendResponse status = xmpp.sendMessage(msg);
            messageSent = (status.getStatusMap().get(jid) == SendResponse.Status.SUCCESS);
        }
        if (!messageSent) {
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);
            try {
                javax.mail.Message email = new MimeMessage(session);
                email.setFrom(new InternetAddress("admin@twitterinmail.appspot.com", "Magic Admin"));
                email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(ADMIN_JID, "Mark Bakker"));
                email.setSubject("Recieved an unkown price code please enter a value for this code");
                email.setText(msgBody);
                Transport.send(email);
            } catch (AddressException e) {
                return;
            } catch (MessagingException e) {
                return;
            } catch (UnsupportedEncodingException e) {
                return;
            }
        }
    }
