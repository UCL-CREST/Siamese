    private boolean verificationEmailSent(User user, HttpServletRequest request) {
        if (user.verifiedEmail) return false;
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        String msgBody = "<h3>ChemVantage Email Verification</h3>" + "To confirm that this is the email address associated with your ChemVantage account, " + "please click the link below or copy/paste it into your web browser address bar.<p>" + request.getRequestURL().toString() + "?UserId=" + user.id + "&Code=" + new Key<User>(User.class, user.id).hashCode() + "  <p>" + "If you did not request this verification, please do not click the link.<br>" + "If you need assistance, please reply to admin@chemvantage.org.<p>" + "Thank you.";
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("admin@chemvantage.org", "ChemVantage"));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(user.email, user.getBothNames()));
            msg.setSubject("ChemVantage Email Verification");
            msg.setContent(msgBody, "text/html");
            Transport.send(msg);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
