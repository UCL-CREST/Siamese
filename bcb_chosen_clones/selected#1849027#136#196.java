    public void send() throws Exception {
        MimeMessage msg;
        java.util.Properties p = new java.util.Properties();
        p.put(MAIL_HOST, host);
        Session session = Session.getInstance(p, null);
        msg = new MimeMessage(session);
        if (from != null) msg.setFrom(new InternetAddress(from));
        msg.setSubject(subject);
        for (int i = 0; i < to.size(); i++) msg.addRecipient(Message.RecipientType.TO, new InternetAddress((String) to.get(i)));
        for (int i = 0; i < cc.size(); i++) msg.addRecipient(Message.RecipientType.CC, new InternetAddress((String) cc.get(i)));
        for (int i = 0; i < bcc.size(); i++) msg.addRecipient(Message.RecipientType.BCC, new InternetAddress((String) bcc.get(i)));
        MimeBodyPart mbp = null;
        Body body = null;
        if ((attachs.size() + resources.size() + bodies.size()) > 1) {
            Multipart mp = null;
            Multipart mpBodies = null;
            Multipart mpResources = null;
            String mpType = Constant.EMPTY;
            if (attachs.size() > 0) mpType = MIXED; else if (resources.size() > 0) mpType = RELATED; else if (bodies.size() > 1) mpType = ALTERNATIVE;
            mp = new MimeMultipart(mpType);
            if (resources.size() > 0 && !mpType.equals(RELATED)) mpResources = new MimeMultipart(RELATED);
            if (bodies.size() > 1 && !mpType.equals(ALTERNATIVE)) mpBodies = new MimeMultipart(ALTERNATIVE);
            for (int i = 0; i < bodies.size(); i++) {
                body = (Body) bodies.get(i);
                mbp = new MimeBodyPart();
                mbp.setContent(body.getContent(), body.getContentType());
                if (mpBodies != null) mpBodies.addBodyPart(mbp); else if (mpResources != null) mpResources.addBodyPart(mbp); else mp.addBodyPart(mbp);
            }
            if (mpBodies != null) {
                mbp = new MimeBodyPart();
                mbp.setContent(mpBodies);
                if (mpResources != null) mpResources.addBodyPart(mbp); else mp.addBodyPart(mbp);
            }
            FileDataSource fds = null;
            for (int i = 0; i < resources.size(); i++) {
                fds = new FileDataSource((String) resources.get(i));
                mbp = new MimeBodyPart();
                mbp.setDataHandler(new DataHandler(fds));
                mbp.setHeader(CONTENT_ID, "<" + fds.getName() + ">");
                if (mpResources != null) mpResources.addBodyPart(mbp); else mp.addBodyPart(mbp);
            }
            if (mpResources != null) {
                mbp = new MimeBodyPart();
                mbp.setContent(mpResources);
                mp.addBodyPart(mbp);
            }
            for (int i = 0; i < attachs.size(); i++) {
                fds = new FileDataSource((String) attachs.get(i));
                mbp = new MimeBodyPart();
                mbp.setDataHandler(new DataHandler(fds));
                mbp.setFileName(fds.getName());
                mp.addBodyPart(mbp);
            }
            msg.setContent(mp);
        } else if (bodies.size() == 1) {
            body = (Body) bodies.get(0);
            msg.setContent(body.getContent(), body.getContentType());
        }
        msg.setSentDate(new java.util.Date());
        Transport.send(msg);
    }
