    private void sendResults(String s) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("fofanova.mn", "boor2vaY");
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("fofanova.mn@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(s));
            message.setSubject("JavaPuzzlers results");
            message.setText(printShortSummary());
            Transport.send(message);
            System.out.println("Done");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (SqlJetException e) {
            e.printStackTrace();
        }
    }
