    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String from = request.getParameter("mailfrom");
        String to = request.getParameter("mailto");
        String subject = request.getParameter("mailsubject");
        String content = request.getParameter("mailcontent");
        if ((from == null) || (to == null) || (subject == null) || (content == null)) {
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/jsp/mail/sendmail.jsp");
            rd.forward(request, response);
            return;
        }
        PrintWriter writer = response.getWriter();
        response.setContentType("text/html");
        writer.println("<html>");
        writer.println("<head>");
        writer.println("<title>Example Mail Sending Results</title>");
        writer.println("</head>");
        writer.println("<body bgcolor=\"white\">");
        try {
            Context initCtx = new InitialContext();
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            Session session = (Session) envCtx.lookup("mail/Session");
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            InternetAddress dests[] = new InternetAddress[] { new InternetAddress(to) };
            message.setRecipients(Message.RecipientType.TO, dests);
            message.setSubject(subject);
            message.setContent(content, "text/plain");
            Transport.send(message);
            writer.println("<strong>Message successfully sent!</strong>");
        } catch (Throwable t) {
            writer.println("<font color=\"red\">");
            writer.println("ENCOUNTERED EXCEPTION:  " + t);
            writer.println("<pre>");
            t.printStackTrace(writer);
            writer.println("</pre>");
            writer.println("</font>");
        }
        writer.println("<br><br>");
        writer.println("<a href=\"jsp/mail/sendmail.jsp\">Create a new message</a><br>");
        writer.println("<a href=\"jsp/index.html\">Back to examples home</a><br>");
        writer.println("</body>");
        writer.println("</html>");
    }
