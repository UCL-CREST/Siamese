    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, NullPointerException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/html");
        response.setHeader("Pragma", "No-cache");
        if (User.checkLoginCookie(request.getCookies())) {
            String from = request.getParameter("from");
            String subject = request.getParameter("subject");
            String to = request.getParameter("to");
            String bcc = request.getParameter("bcc");
            String cc = request.getParameter("cc");
            String content = request.getParameter("content");
            String lid = request.getParameter("lid");
            String mid = request.getParameter("mid");
            String ref = request.getParameter("ref");
            try {
                Properties props = new Properties();
                props.put("mail.smtp.host", "localhost");
                Session mailsession = Session.getDefaultInstance(props, null);
                Message email = new MimeMessage(mailsession);
                email.setFrom(new InternetAddress(from));
                InternetAddress[] address = { new InternetAddress(to) };
                email.setRecipients(Message.RecipientType.TO, address);
                if (!bcc.equals("")) {
                    InternetAddress[] bccaddress = { new InternetAddress(bcc) };
                    email.addRecipients(Message.RecipientType.BCC, bccaddress);
                }
                if (!cc.equals("")) {
                    InternetAddress[] ccaddress = { new InternetAddress(cc) };
                    email.addRecipients(Message.RecipientType.CC, ccaddress);
                }
                email.setSubject(subject);
                email.setText(content);
                Transport.send(email);
                HTMLSettings.printHeader(out, "Send Successful");
                if (ref.equals("showMessage")) out.println("Message sent successfully.  <a href=my?action=showMessage&lid=" + lid + "&mid=" + mid + ">Return</a> to lists."); else out.println("Message sent successfully.  <a href=my?action=showList&lid=" + lid + ">Return</a> to lists.");
            } catch (MessagingException e) {
                System.out.println(e);
            }
        } else {
            HTMLSettings.printHeader(out, "Please log in");
            out.println("<p>Please login <a href=login?referrer=" + request.getRequestURI() + ">here</a></p>");
        }
    }
