    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");
        String kime = req.getParameter("kime");
        String konu = req.getParameter("konu");
        String mesaj = req.getParameter("mesaj");
        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");
        String durum = "Durum : ";
        Properties properties = new Properties();
        Session session = Session.getDefaultInstance(properties);
        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress("tacmuharrem@gmail.com"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(kime));
            message.setSubject(konu);
            message.setContent(mesaj, "text/html");
            Transport.send(message);
            durum += " emailiniz gönderildi";
        } catch (Exception e) {
            log.severe(e.getMessage());
            durum += " hata oluştu , hata kodu : " + e.getMessage();
        }
        resp.getWriter().println(durum);
    }
