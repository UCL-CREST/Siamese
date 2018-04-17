    @Override
    public int sendCode(String to) {
        Random random = new Random();
        int randomInt = random.nextInt(999999);
        System.out.println(randomInt);
        Properties props = System.getProperties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress("kryo.afh@gmail.com", "Audictiv"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Audictiv - Forgot password - Code");
            message.setContent("Here is the code: " + randomInt, "text/html");
            Transport.send(message);
            return randomInt;
        } catch (AddressException e3) {
            e3.printStackTrace();
            return -1;
        } catch (MessagingException e3) {
            e3.printStackTrace();
            return -1;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return -1;
        }
    }
