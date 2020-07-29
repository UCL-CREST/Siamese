    private long send() throws Exception {
        MimeMessage msg = new MimeMessage(session);
        msg.setSender(new InternetAddress("test@socket.org"));
        msg.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress("test2@socket.org"));
        msg.setText(data);
        long start = System.currentTimeMillis();
        Transport.send(msg);
        long elapsed = System.currentTimeMillis() - start;
        System.out.println(elapsed);
        return elapsed;
    }
