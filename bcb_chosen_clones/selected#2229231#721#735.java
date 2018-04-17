    public static void sendMail(Session session, String from, String to, String subject, String contentType, Object content) throws NamingException, MessagingException {
        final String ENCODING = "ISO8859_1";
        MimeMessage message = new MimeMessage(session);
        if (from != null) message.setFrom(new InternetAddress(from));
        {
            String tk;
            for (StringTokenizer st = new StringTokenizer(to, ";, \t\n\r\f"); st.hasMoreTokens(); ) {
                tk = st.nextToken();
                message.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(tk));
            }
        }
        message.setSubject(subject, ENCODING);
        if (contentType == null || "text/plain".equals(contentType)) message.setText((String) content, ENCODING); else message.setContent(content, contentType);
        Transport.send(message);
    }
