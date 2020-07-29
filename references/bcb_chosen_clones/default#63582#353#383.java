    private void send(HttpServletRequest req, HttpServletResponse res, ServletOutputStream out, HttpSession ssn) throws IOException {
        String to = req.getParameter("to");
        String cc = req.getParameter("cc");
        String subj = req.getParameter("subject");
        String text = req.getParameter("text");
        try {
            MailUserData mud = getMUD(ssn);
            if (mud == null) throw new Exception("trying to send, but not logged in");
            Message msg = new MimeMessage(mud.getSession());
            InternetAddress[] toAddrs = null, ccAddrs = null;
            if (to != null) {
                toAddrs = InternetAddress.parse(to, false);
                msg.setRecipients(Message.RecipientType.TO, toAddrs);
            } else throw new MessagingException("No \"To\" address specified");
            if (cc != null) {
                ccAddrs = InternetAddress.parse(cc, false);
                msg.setRecipients(Message.RecipientType.CC, ccAddrs);
            }
            if (subj != null) msg.setSubject(subj);
            URLName u = mud.getURLName();
            msg.setFrom(new InternetAddress(u.getUsername() + "@" + u.getHost()));
            if (text != null) msg.setText(text);
            Transport.send(msg);
            out.println("<h1>Message sent successfully</h1></body></html>");
            out.close();
        } catch (Exception mex) {
            out.println("<h1>Error sending message.</h1>");
            out.println(mex.toString());
            out.println("<br></body></html>");
        }
    }
