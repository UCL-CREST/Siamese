    public HTMLDocument handleURL(String suburl, HTTPSession sess1, HTTPRequestHeader head) throws WebMailException, ServletException {
        if (sess1 == null) {
            throw new WebMailException("No session was given. If you feel this is incorrect, please contact your system administrator");
        }
        UserSession session = (UserSession) sess1;
        UserData user = session.getUser();
        HTMLDocument content;
        Locale locale = user.getPreferredLocale();
        session.storeMessage(head);
        if (head.isContentSet("SEND")) {
            try {
                MimeMessage msg = new MimeMessage(mailsession);
                Address from[] = new Address[1];
                try {
                    from[0] = new InternetAddress(TranscodeUtil.transcodeThenEncodeByLocale(session.getUser().getEmail(), null, locale), TranscodeUtil.transcodeThenEncodeByLocale(session.getUser().getFullName(), null, locale));
                } catch (UnsupportedEncodingException e) {
                    store.getLogger().log(Logger.LOG_WARN, "Unsupported Encoding while trying to send message: " + e.getMessage());
                    from[0] = new InternetAddress(session.getUser().getEmail(), session.getUser().getFullName());
                }
                StringTokenizer t;
                try {
                    t = new StringTokenizer(TranscodeUtil.transcodeThenEncodeByLocale(head.getContent("TO"), null, locale).trim(), ",");
                } catch (UnsupportedEncodingException e) {
                    store.getLogger().log(Logger.LOG_WARN, "Unsupported Encoding while trying to send message: " + e.getMessage());
                    t = new StringTokenizer(head.getContent("TO").trim(), ",;");
                }
                if (t.countTokens() < 1) {
                    throw new MessagingException("The recipient field must not be empty!");
                }
                Address to[] = new Address[t.countTokens()];
                int i = 0;
                while (t.hasMoreTokens()) {
                    to[i] = new InternetAddress(t.nextToken().trim());
                    i++;
                }
                try {
                    t = new StringTokenizer(TranscodeUtil.transcodeThenEncodeByLocale(head.getContent("CC"), null, locale).trim(), ",");
                } catch (UnsupportedEncodingException e) {
                    store.getLogger().log(Logger.LOG_WARN, "Unsupported Encoding while trying to send message: " + e.getMessage());
                    t = new StringTokenizer(head.getContent("CC").trim(), ",;");
                }
                Address cc[] = new Address[t.countTokens()];
                i = 0;
                while (t.hasMoreTokens()) {
                    cc[i] = new InternetAddress(t.nextToken().trim());
                    i++;
                }
                try {
                    t = new StringTokenizer(TranscodeUtil.transcodeThenEncodeByLocale(head.getContent("BCC"), null, locale).trim(), ",");
                } catch (UnsupportedEncodingException e) {
                    store.getLogger().log(Logger.LOG_WARN, "Unsupported Encoding while trying to send message: " + e.getMessage());
                    t = new StringTokenizer(head.getContent("BCC").trim(), ",;");
                }
                Address bcc[] = new Address[t.countTokens()];
                i = 0;
                while (t.hasMoreTokens()) {
                    bcc[i] = new InternetAddress(t.nextToken().trim());
                    i++;
                }
                session.setSent(false);
                msg.addFrom(from);
                if (to.length > 0) {
                    msg.addRecipients(Message.RecipientType.TO, to);
                }
                if (cc.length > 0) {
                    msg.addRecipients(Message.RecipientType.CC, cc);
                }
                if (bcc.length > 0) {
                    msg.addRecipients(Message.RecipientType.BCC, bcc);
                }
                msg.addHeader("X-Mailer", WebMailServer.getVersion() + ", " + getName() + " plugin v" + getVersion());
                String subject = null;
                if (!head.isContentSet("SUBJECT")) {
                    subject = "no subject";
                } else {
                    try {
                        subject = TranscodeUtil.transcodeThenEncodeByLocale(head.getContent("SUBJECT"), "ISO8859_1", locale);
                    } catch (UnsupportedEncodingException e) {
                        store.getLogger().log(Logger.LOG_WARN, "Unsupported Encoding while trying to send message: " + e.getMessage());
                        subject = head.getContent("SUBJECT");
                    }
                }
                msg.addHeader("Subject", subject);
                if (head.isContentSet("REPLY-TO")) {
                    msg.addHeader("Reply-To", TranscodeUtil.transcodeThenEncodeByLocale(head.getContent("REPLY-TO"), "ISO8859_1", locale));
                }
                msg.setSentDate(new Date(System.currentTimeMillis()));
                String contnt = head.getContent("BODY");
                String charset = "utf-8";
                MimeMultipart cont = new MimeMultipart();
                MimeBodyPart txt = new MimeBodyPart();
                contnt = new String(contnt.getBytes("ISO8859_1"), "UTF-8");
                if (locale.getLanguage().equals("zh") && locale.getCountry().equals("TW")) {
                    txt.setText(contnt, "Big5");
                    txt.setHeader("Content-Type", "text/plain; charset=\"Big5\"");
                    txt.setHeader("Content-Transfer-Encoding", "quoted-printable");
                } else {
                    txt.setText(contnt, "utf-8");
                    txt.setHeader("Content-Type", "text/plain; charset=\"utf-8\"");
                    txt.setHeader("Content-Transfer-Encoding", "quoted-printable");
                }
                cont.addBodyPart(txt);
                if (store.getConfig("ADVERTISEMENT ATTACH").equals("YES")) {
                    MimeBodyPart adv = new MimeBodyPart();
                    String file = "";
                    if (store.getConfig("ADVERTISEMENT SIGNATURE PATH").startsWith("/")) {
                        file = store.getConfig("ADVERTISEMENT SIGNATURE PATH");
                    } else {
                        file = parent.getProperty("webmail.data.path") + System.getProperty("file.separator") + store.getConfig("ADVERTISEMENT SIGNATURE PATH");
                    }
                    String advcont = "";
                    try {
                        BufferedReader fin = new BufferedReader(new FileReader(file));
                        String line = fin.readLine();
                        while (line != null && !line.equals("")) {
                            advcont += line + "\n";
                            line = fin.readLine();
                        }
                        fin.close();
                    } catch (IOException ex) {
                    }
                    if (locale.getLanguage().equals("zh") && locale.getCountry().equals("TW")) {
                        advcont = new String(advcont.getBytes(), "Big5");
                        adv.setText(advcont, "Big5");
                        adv.setHeader("Content-Type", "text/plain; charset=\"Big5\"");
                        adv.setHeader("Content-Transfer-Encoding", "quoted-printable");
                    } else {
                        advcont = new String(advcont.getBytes(), "UTF-8");
                        adv.setText(advcont, "utf-8");
                        adv.setHeader("Content-Type", "text/plain; charset=\"utf-8\"");
                        adv.setHeader("Content-Transfer-Encoding", "quoted-printable");
                    }
                    cont.addBodyPart(adv);
                }
                Enumeration atts = session.getAttachments().keys();
                while (atts.hasMoreElements()) {
                    ByteStore bs = session.getAttachment((String) atts.nextElement());
                    InternetHeaders ih = new InternetHeaders();
                    ih.addHeader("Content-Type", bs.getContentType());
                    ih.addHeader("Content-Transfer-Encoding", "BASE64");
                    PipedInputStream pin = new PipedInputStream();
                    PipedOutputStream pout = new PipedOutputStream(pin);
                    StreamConnector sconn = new StreamConnector(pin, (int) (bs.getSize() * 1.6) + 1000);
                    BufferedOutputStream encoder = new BufferedOutputStream(MimeUtility.encode(pout, "BASE64"));
                    encoder.write(bs.getBytes());
                    encoder.flush();
                    encoder.close();
                    MimeBodyPart att1 = new MimeBodyPart(ih, sconn.getResult().getBytes());
                    att1.addHeader("Content-Type", bs.getContentType());
                    att1.setDescription(bs.getDescription(), "utf-8");
                    String fileName = bs.getName();
                    System.err.println("fileName: " + fileName);
                    if (locale.getLanguage().equals("zh") && locale.getCountry().equals("TW")) {
                        fileName = MimeUtility.encodeText(fileName, "Big5", null);
                    }
                    att1.setFileName(fileName);
                    cont.addBodyPart(att1);
                }
                msg.setContent(cont);
                msg.saveChanges();
                boolean savesuccess = true;
                msg.setHeader("Message-ID", session.getUserModel().getWorkMessage().getAttribute("msgid"));
                if (session.getUser().wantsSaveSent()) {
                    String folderhash = session.getUser().getSentFolder();
                    try {
                        Folder folder = session.getFolder(folderhash);
                        Message[] m = new Message[1];
                        m[0] = msg;
                        folder.appendMessages(m);
                    } catch (MessagingException e) {
                        savesuccess = false;
                    } catch (NullPointerException e) {
                        savesuccess = false;
                    }
                }
                boolean sendsuccess = false;
                try {
                    Transport.send(msg);
                    Address sent[] = new Address[to.length + cc.length + bcc.length];
                    int c1 = 0;
                    int c2 = 0;
                    for (c1 = 0; c1 < to.length; c1++) {
                        sent[c1] = to[c1];
                    }
                    for (c2 = 0; c2 < cc.length; c2++) {
                        sent[c1 + c2] = cc[c2];
                    }
                    for (int c3 = 0; c3 < bcc.length; c3++) {
                        sent[c1 + c2 + c3] = bcc[c3];
                    }
                    sendsuccess = true;
                    throw new SendFailedException("success", new Exception("success"), sent, null, null);
                } catch (SendFailedException e) {
                    session.handleTransportException(e);
                }
                content = new XHTMLDocument(session.getModel(), store.getStylesheet("sendresult.xsl", user.getPreferredLocale(), user.getTheme()));
                if (sendsuccess) session.clearWork();
            } catch (Exception e) {
                e.printStackTrace();
                store.getLogger().log(Logger.LOG_ERR, e);
                throw new DocumentNotFoundException("Could not send message. (Reason: " + e.getMessage() + ")");
            }
        } else if (head.isContentSet("ATTACH")) {
            content = parent.getURLHandler().handleURL("/compose/attach", session, head);
        } else {
            throw new DocumentNotFoundException("Could not send message. (Reason: No content given)");
        }
        return content;
    }
