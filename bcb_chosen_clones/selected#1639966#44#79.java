    public void send(RecordAlarm alarm, User user) throws MessagingException {
        if (alarm == null) throw new MessagingException("Alarm cannot be null");
        Record r = alarm.getRecord();
        if (r == null) throw new MessagingException("Alarm must contain a record");
        String to = "\"" + user.getName() + "\" <" + ((UserProfile) user.getProfile()).getEmail() + ">";
        String description = r.getDescription();
        Locale loc = user.getLocale() == null ? Locale.getDefault() : user.getLocale();
        ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME, loc);
        String title = "Message Alarm from {0}";
        String body = "-----------------\nRecord information\n start: {0,time} {0,date}\nend: {1,time} {1,date}.\nowner: {2}\n-----------------\nRecord content:\n";
        String body_after = "\n-----------------\nTo access the service point your browser to {0}";
        if (bundle != null) {
            title = bundle.getString("alarm.mail.title");
            body = bundle.getString("alarm.mail.pre_body");
            body_after = bundle.getString("alarm.mail.post_body");
        }
        title = MessageFormat.format(title, new Object[] { new String(r.getOwner().getLogin()) });
        MessageFormat mf = new MessageFormat(body);
        mf.setLocale(loc);
        mf.applyPattern(body);
        description = mf.format(new Object[] { r.getStartTime(), r.getEndTime(), r.getOwner().getLogin() }) + "\n\n" + description;
        if (url != null) description += "\n\n" + MessageFormat.format(body_after, new Object[] { url });
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(title);
        message.setText(description);
        try {
            message.setHeader("X-Mailer", MimeUtility.encodeText(CREDITS));
        } catch (Exception e) {
            logger.debug("Could not set credits", e);
        }
        message.setSentDate(new Date());
        Transport.send(message);
    }
