    boolean mergeAuthCodeSent(User user, HttpServletRequest request) {
        if (!user.verifiedEmail) return false;
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        try {
            User fromAccountUser = ofy.get(User.class, request.getParameter("FromAccount"));
            int code = Math.abs((new Key<User>(User.class, fromAccountUser.id).toString() + new Key<User>(User.class, user.id)).toString().hashCode());
            String msgBody = "<h3>ChemVantage Account Merge Request</h3>" + "To complete the requested account merge request, please copy/paste the following " + "authorization number into the request form:<p>" + code + "<p>" + "If you did not request this authorization code, please ignore this message.<br>" + "For assistance, please reply to admin@chemvantage.org.<p>" + "Thank you.";
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("admin@chemvantage.org", "ChemVantage"));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(fromAccountUser.email, fromAccountUser.getBothNames()));
            msg.setSubject("ChemVantage Authorization Number");
            msg.setContent(msgBody, "text/html");
            Transport.send(msg);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
