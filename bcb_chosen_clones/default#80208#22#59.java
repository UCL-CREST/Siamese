    public void actionPerformed(ActionEvent e) {
        String dest, mittente, oggetto, test;
        dest = m.getdestinatario();
        oggetto = m.getoggetto();
        test = m.getcorpo() + "\n" + m.getcorpo1() + "\n" + m.getcorpo2();
        mittente = m.getmittente();
        int pos = 0;
        try {
            pos = metodo(mittente);
        } catch (Exception extv) {
        }
        String rip = (String) m.getbox();
        int a = Integer.parseInt(rip);
        try {
            for (int i = 0; i < a; i++) {
                mittente = m.getmittente();
                if (m.getceck() != null) {
                    String mitt1 = mittente.substring(0, pos);
                    byte[] f = mittente.getBytes();
                    String mitt2 = mittente.substring(pos, f.length);
                    mittente = mitt1 + i + mitt2;
                }
                String prova = (String) m.getoperator();
                Properties props = new Properties();
                props.put("mail.smtp.host", isp(prova));
                Session session = Session.getDefaultInstance(props);
                MimeMessage message = new MimeMessage(session);
                message.setSubject(oggetto);
                message.setText(test);
                InternetAddress fromAddress = new InternetAddress(mittente);
                InternetAddress toAddress = new InternetAddress(dest);
                message.setFrom(fromAddress);
                message.setRecipient(Message.RecipientType.TO, toAddress);
                Transport.send(message);
            }
        } catch (Exception ext) {
        }
    }
