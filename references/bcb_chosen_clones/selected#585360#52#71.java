    public void send_mail(String s1, String s2, String s3) throws Exception {
        String[] tt = s1.split(",");
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        String sdt = "";
        TimeZone tz = TimeZone.getTimeZone("America/Montreal");
        SimpleDateFormat parser = new SimpleDateFormat("MMM dd, yyyy 'at' HH:mm:ss z");
        Date d = Calendar.getInstance(TimeZone.getTimeZone("EST")).getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd  HH:mm:ss z'('Z')'");
        formatter.setTimeZone(tz);
        sdt = formatter.format(d);
        s2 = s2 + " " + sdt;
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("ymdata@gmail.com", "UFOS Web"));
        for (int i = 0; i < tt.length; i++) msg.addRecipient(Message.RecipientType.TO, new InternetAddress(tt[i], tt[i]));
        msg.setSubject(s2);
        msg.setHeader("Content-type:", "text/html;charset=ISO-8859-1");
        msg.setText(s3);
        Transport.send(msg);
    }
