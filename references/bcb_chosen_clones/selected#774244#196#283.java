    public String sendMails(HashMap mailMap, MailVO info, ArrayList aryList) {
        Properties props = new Properties();
        String smtpServer = new String("");
        String fromEmail = new String();
        String toEmail = new String();
        String ccEmail = new String("");
        String bccEmail = new String("");
        String mailNote = new String("");
        String strRealPath = new String("");
        int iCheck = 0;
        Pop3ConfigVO configView = (Pop3ConfigVO) mailMap.get("view");
        mailNote = (String) mailMap.get("mainNote");
        boolean bCheck = false;
        protocol = "smtp";
        smtpServer = setSmtpServer(configView);
        setSmtpPort(configView);
        bCheck = configView.getSmtp_auth().equals("1");
        setSendMailProperty(props, smtpServer, bCheck);
        fromEmail = configView.getShow_address();
        toEmail = info.getReceive_address();
        ccEmail = info.getCopy_send();
        bccEmail = info.getDense_send();
        HashMap errMap = new HashMap();
        errMap.put("server", configView.getSmtp_server());
        errMap.put("from", fromEmail);
        if (bCheck) {
            userID = configView.getSmtp_name();
            password = configView.getSmtp_pwd();
        }
        try {
            Session mailSession = null;
            mailSession = Session.getInstance(props, null);
            mailSession = setMailSession(props, bCheck);
            Message msg = new MimeMessage(mailSession);
            msg.setFrom(new InternetAddress(fromEmail));
            if (!toEmail.equals("")) {
                ++iCheck;
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            }
            if (!ccEmail.equals("")) {
                ++iCheck;
                msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccEmail));
            }
            if (!bccEmail.equals("")) {
                ++iCheck;
                msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bccEmail));
            }
            if (iCheck == 0) return "";
            msg.setSentDate(new Date());
            msg.setSubject(info.getTitle());
            Multipart mult = new MimeMultipart();
            MimeBodyPart mBody = new MimeBodyPart();
            mBody.setText(mailNote);
            mult.addBodyPart(mBody);
            setAttachBodyPart(aryList, strRealPath, mult);
            msg.setContent(mult);
            Transport transport = mailSession.getTransport("smtp");
            transport.connect(smtpServer, Integer.parseInt(port), userID, password);
            transport.send(msg);
        } catch (javax.mail.SendFailedException e) {
            e.printStackTrace();
            boolean bErr = true;
            Address list[] = e.getInvalidAddresses();
            if (list != null) {
                errMap.put("list", list);
                bErr = false;
            }
            Address unlist[] = e.getValidUnsentAddresses();
            if (unlist != null) {
                errMap.put("unlist", unlist);
                bErr = false;
            }
            Address sentlist[] = e.getValidSentAddresses();
            if (sentlist != null) {
                errMap.put("sentlist", unlist);
                bErr = false;
            }
            this.procSmtpError(errMap, bErr);
        } catch (javax.mail.AuthenticationFailedException e) {
            e.printStackTrace();
            this.procSmtpError(errMap, true);
        } catch (javax.mail.MessagingException e) {
            e.printStackTrace();
            this.procSmtpError(errMap, true);
        }
        mailMap.put("errorMap", errMap);
        return this.strBufErr.toString();
    }
