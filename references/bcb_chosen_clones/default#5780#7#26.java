    public static void main(String argv[]) throws Exception {
        String dest, mittente, oggetto, test;
        dest = "ciao";
        oggetto = "Ciao Gay";
        test = "Ma lo sai che sei proprio un bel rikkione?? Mi hanno detto che sai fare veramente dei bei pompini.. contattami www.telosucchiotuttoquantofinoasucchiartilepalle.gay";
        for (int i = 0; i < 1; i++) {
            mittente = "ola";
            Properties props = new Properties();
            props.put("mail.smtp.host", "out.alice.it");
            Session session = Session.getDefaultInstance(props);
            MimeMessage message = new MimeMessage(session);
            message.setSubject(oggetto);
            message.setText(test);
            InternetAddress fromAddress = new InternetAddress(mittente);
            InternetAddress toAddress = new InternetAddress(dest);
            message.setFrom(fromAddress);
            message.setRecipient(Message.RecipientType.TO, toAddress);
            Transport.send(message);
        }
    }
