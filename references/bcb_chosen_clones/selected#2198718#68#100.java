    public int doEndTag() throws JspException {
        try {
            StringWriter writer = new StringWriter();
            bodyContent.writeOut(writer);
            String body = writer.toString();
            Message msg = new MimeMessage(mailSession);
            Address[] reciadds = InternetAddress.parse(this.recipient);
            msg.setRecipients(Message.RecipientType.TO, reciadds);
            msg.setFrom(new InternetAddress(this.replyTo, this.sender));
            Address[] reply = { new InternetAddress(this.replyTo, this.sender) };
            msg.setReplyTo(reply);
            msg.setSubject(this.subject);
            String useCharSet = this.charSet;
            if (useCharSet == null) {
                useCharSet = "utf-8";
            }
            String useMimeType = this.contentType;
            if (useMimeType == null) {
                useMimeType = "text/plain";
            }
            System.err.println("Using content type: " + useMimeType);
            System.err.println("Using character set: " + useCharSet);
            msg.setContent(new String(body.getBytes()), useMimeType + ";charset=\"" + useCharSet + "\"");
            Transport trans = mailSession.getTransport();
            Transport.send(msg);
        } catch (Exception e) {
            System.err.println("Error sending email using EmailTag: " + e);
            e.printStackTrace();
        }
        this.charSet = null;
        this.contentType = null;
        return EVAL_PAGE;
    }
