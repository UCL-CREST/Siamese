    public boolean sendEmailValidation(int userID, String emailAddress, String activationKey) {
        File file = new File("./data/activationemail.txt");
        if (!file.exists()) {
            CampaignData.mwlog.errLog("/data/activationemail.txt does not exist");
            return false;
        }
        String line = "";
        String subject = "";
        String mailFrom = "";
        StringBuilder body = new StringBuilder();
        try {
            FileInputStream fis = new FileInputStream(file);
            BufferedReader dis = new BufferedReader(new InputStreamReader(fis));
            while (dis.ready()) {
                line = dis.readLine();
                if (line.startsWith("[SUBJECT]")) {
                    subject = line;
                    subject = subject.replace("[SUBJECT]", "");
                } else if (line.startsWith("[MAILFROM]")) {
                    mailFrom = line;
                    mailFrom = mailFrom.replace("[MAILFROM]", "");
                } else {
                    body.append(line + "\n");
                }
            }
            dis.close();
            fis.close();
        } catch (FileNotFoundException fnfe) {
            CampaignData.mwlog.errLog("FileNotFoundException in PhpBBConnector.sendActivationEmail: " + fnfe.getMessage());
            return false;
        } catch (IOException ioe) {
            CampaignData.mwlog.errLog("IOException in PhpBBConnector.sendActivationEmail: " + ioe.getMessage());
            return false;
        }
        String bodyString = body.toString().replaceAll("%USERACTKEY%", activationKey);
        Properties props = new Properties();
        String smtphost = null;
        if ((smtphost = CampaignMain.cm.getServer().getConfigParam("MAILHOST")) == null) {
            CampaignData.mwlog.errLog("MAILHOST not set in serverconfig");
            CampaignMain.cm.doSendModMail("NOTE", "MAILHOST not set in serverconfig.");
            return false;
        }
        String protocol = "smtp";
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", Boolean.toString(Boolean.parseBoolean(CampaignMain.cm.getServer().getConfigParam("MAILPASSREQUIRED"))));
        props.put("mail.smtp.host", smtphost);
        props.put("mail.from", mailFrom);
        Session session = Session.getInstance(props, null);
        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom();
            msg.setRecipients(Message.RecipientType.TO, emailAddress);
            msg.setSubject(subject);
            msg.setSentDate(new Date());
            msg.setText(bodyString);
            if (Boolean.parseBoolean(props.get("mail.smtp.auth").toString())) {
                Transport trans = session.getTransport(protocol);
                trans.connect(CampaignMain.cm.getServer().getConfigParam("MAILUSER"), CampaignMain.cm.getServer().getConfigParam("MAILPASS"));
                trans.sendMessage(msg, msg.getAllRecipients());
            } else {
                Transport.send(msg);
            }
        } catch (MessagingException e) {
            CampaignData.mwlog.errLog("Email send failed:");
            CampaignData.mwlog.errLog(e);
            return false;
        }
        return true;
    }
