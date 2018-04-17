    void sendSMSConfirmationCode(String address, String code) {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        Message msg = new MimeMessage(session);
        try {
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(address));
            msg.setFrom(new InternetAddress("admin@chemvantage.org", "ChemVantage"));
            msg.setSubject("ChemVantage.org SMS Registration");
            String messageText = "Enter this on the Scores page: " + code;
            msg.setText(messageText);
            Transport.send(msg);
        } catch (Exception e) {
        }
    }
