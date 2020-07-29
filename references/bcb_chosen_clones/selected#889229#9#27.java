    public static void doPost(String from, String subject, String body) {
        String mailHost = "85.122.23.145";
        String to = " ioana.creanga@info.uaic.ro";
        if ((from != null) && (to != null) && (subject != null) && (body != null)) {
            try {
                Properties props = System.getProperties();
                props.put("mail.smtp.host", mailHost);
                Session session = Session.getInstance(props, null);
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(from));
                message.setRecipients(Message.RecipientType.TO, new InternetAddress[] { new InternetAddress(to) });
                message.setSubject(subject);
                message.setContent(body, "text/plain");
                Transport.send(message);
            } catch (Throwable t) {
                System.out.println("Eroor: " + t.toString());
            }
        }
    }
