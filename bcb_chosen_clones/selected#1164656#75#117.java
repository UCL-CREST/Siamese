    public static void send(String from, Hashtable recipient, String host, String subject, String msgText, boolean debug) {
        Properties props = System.getProperties();
        props.put("mail.smtp.host", host);
        Session session = Session.getInstance(props, null);
        session.setDebug(debug);
        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            Enumeration renum = recipient.keys();
            while (renum.hasMoreElements()) {
                Object rkey = renum.nextElement();
                if (rkey instanceof Message.RecipientType) {
                    Message.RecipientType rtype = (Message.RecipientType) rkey;
                    Object rvo = recipient.get(rkey);
                    InternetAddress[] addresses = null;
                    if (rvo instanceof String[]) {
                        String[] rvs = (String[]) rvo;
                        addresses = new InternetAddress[rvs.length];
                        for (int rr = 0; rr < rvs.length; rr++) addresses[rr] = new InternetAddress(rvs[rr]);
                    } else if (rvo instanceof InternetAddress[]) {
                        addresses = (InternetAddress[]) rvo;
                    } else if (rvo instanceof InternetAddress) {
                        InternetAddress[] addrs = { (InternetAddress) rvo };
                        addresses = addrs;
                    } else if (rvo instanceof String) {
                        InternetAddress[] addrs = { new InternetAddress("" + rvo) };
                        addresses = addrs;
                    }
                    msg.setRecipients(rtype, addresses);
                }
            }
            msg.setSubject(subject);
            msg.setText(msgText);
            msg.setSentDate(new Date());
            Transport.send(msg);
        } catch (MessagingException mex) {
            mex.printStackTrace();
            Exception ex = null;
            if ((ex = mex.getNextException()) != null) {
                ex.printStackTrace();
            }
        }
    }
