    public static void sendPasswordTo(Taikhoan user) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("thuanbsf@gmail.com", "daicaqwefjkd");
            }
        });
        try {
            Random r = new Random(6);
            int mkNew = r.nextInt(999999);
            String mkHash = MyUtil.encodeMD5(String.valueOf(mkNew));
            user.setMatKhau(mkHash);
            TaiKhoanDAO.capNhatTaiKhoan(user);
            Message message = new MimeMessage(session);
            message.setHeader("Content-Type", "text/plain; charset=UTF-8");
            message.setFrom(new InternetAddress("N2TCorp@n2t.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getThongtintaikhoans().getEmail()));
            message.setSubject("Web  N2T - phuc hoi mat khau");
            String content = String.format("Xin chào " + user.getThongtintaikhoans().getHoTen() + "!\n" + "\nBạn vừa yêu cầu phục hồi mật khẩu từ N2T. Đây là thông tin tài khỏan của bạn " + "\nTên tài khỏan: " + user.getMaTaiKhoan() + "" + "\nMật khẩu: " + String.valueOf(mkNew) + "\nVui lòng đăng nhập bằng mật khẩu trên và reset mật khẩu." + "\nThân chào!" + "\nWebsite N2T-Corp");
            message.setContent(content, "text/plain; charset=UTF-8");
            Transport.send(message);
            System.out.println("Done");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
