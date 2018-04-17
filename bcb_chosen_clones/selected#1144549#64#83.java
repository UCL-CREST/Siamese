    public void send(String mailAddress) throws EJbsMail, AddressException, MessagingException {
        if (getAccount() == null) throw new EJbsMail(EJbsMail.ET_NOACCOUNT);
        if ((mailAddress == null) || (mailAddress.trim().equals(""))) throw new EJbsMail(EJbsMail.ET_NORECIVER);
        if ((getSubject() == null) || (getSubject().trim().equals(""))) throw new EJbsMail(EJbsMail.ET_NOSUBJECT);
        if ((getText() == null) || (getText().trim().equals(""))) throw new EJbsMail(EJbsMail.ET_NOTEXT);
        JbsMailAuthenticator auth = new JbsMailAuthenticator(getAccount().getName(), getAccount().getPasswort());
        Properties properties = new Properties();
        properties.put("mail.smtp.host", getAccount().getMailHost());
        properties.put("mail.smtp.auth", "true");
        Session session = Session.getInstance(properties, auth);
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(account.getMailAddress()));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailAddress, false));
        msg.setSubject(subject);
        msg.setText(text);
        msg.setHeader("X-Mailer", "OpenJBS-Mailer <http://openjbs.gs-networks.de>");
        msg.setSentDate(new Date());
        Transport.send(msg);
        logger.info("Send Mail to " + mailAddress + " with Subject: " + subject);
    }
