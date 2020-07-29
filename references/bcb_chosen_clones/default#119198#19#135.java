    public static void main(String[] argv) {
        String to, subject = null, from = null, cc = null, bcc = null, url = null;
        String mailhost = null;
        String mailer = "msgsend";
        String file = null;
        String protocol = null, host = null, user = null, password = null;
        String record = null;
        boolean debug = false;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int optind;
        for (optind = 0; optind < argv.length; optind++) {
            if (argv[optind].equals("-T")) {
                protocol = argv[++optind];
            } else if (argv[optind].equals("-H")) {
                host = argv[++optind];
            } else if (argv[optind].equals("-U")) {
                user = argv[++optind];
            } else if (argv[optind].equals("-P")) {
                password = argv[++optind];
            } else if (argv[optind].equals("-M")) {
                mailhost = argv[++optind];
            } else if (argv[optind].equals("-f")) {
                record = argv[++optind];
            } else if (argv[optind].equals("-a")) {
                file = argv[++optind];
            } else if (argv[optind].equals("-s")) {
                subject = argv[++optind];
            } else if (argv[optind].equals("-o")) {
                from = argv[++optind];
            } else if (argv[optind].equals("-c")) {
                cc = argv[++optind];
            } else if (argv[optind].equals("-b")) {
                bcc = argv[++optind];
            } else if (argv[optind].equals("-L")) {
                url = argv[++optind];
            } else if (argv[optind].equals("-d")) {
                debug = true;
            } else if (argv[optind].equals("--")) {
                optind++;
                break;
            } else if (argv[optind].startsWith("-")) {
                System.out.println("Usage: msgsend [[-L store-url] | [-T prot] [-H host] [-U user] [-P passwd]]");
                System.out.println("\t[-s subject] [-o from-address] [-c cc-addresses] [-b bcc-addresses]");
                System.out.println("\t[-f record-mailbox] [-M transport-host] [-a attach-file] [-d] [address]");
                System.exit(1);
            } else {
                break;
            }
        }
        try {
            if (optind < argv.length) {
                to = argv[optind];
                System.out.println("To: " + to);
            } else {
                System.out.print("To: ");
                System.out.flush();
                to = in.readLine();
            }
            if (subject == null) {
                System.out.print("Subject: ");
                System.out.flush();
                subject = in.readLine();
            } else {
                System.out.println("Subject: " + subject);
            }
            Properties props = System.getProperties();
            if (mailhost != null) props.put("mail.smtp.host", mailhost);
            Session session = Session.getInstance(props, null);
            if (debug) session.setDebug(true);
            Message msg = new MimeMessage(session);
            if (from != null) msg.setFrom(new InternetAddress(from)); else msg.setFrom();
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            if (cc != null) msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc, false));
            if (bcc != null) msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bcc, false));
            msg.setSubject(subject);
            String text = collect(in);
            if (file != null) {
                MimeBodyPart mbp1 = new MimeBodyPart();
                mbp1.setText(text);
                MimeBodyPart mbp2 = new MimeBodyPart();
                mbp2.attachFile(file);
                MimeMultipart mp = new MimeMultipart();
                mp.addBodyPart(mbp1);
                mp.addBodyPart(mbp2);
                msg.setContent(mp);
            } else {
                msg.setText(text);
            }
            msg.setHeader("X-Mailer", mailer);
            msg.setSentDate(new Date());
            Transport.send(msg);
            System.out.println("\nMail was sent successfully.");
            if (record != null) {
                Store store = null;
                if (url != null) {
                    URLName urln = new URLName(url);
                    store = session.getStore(urln);
                    store.connect();
                } else {
                    if (protocol != null) store = session.getStore(protocol); else store = session.getStore();
                    if (host != null || user != null || password != null) store.connect(host, user, password); else store.connect();
                }
                Folder folder = store.getFolder(record);
                if (folder == null) {
                    System.err.println("Can't get record folder.");
                    System.exit(1);
                }
                if (!folder.exists()) folder.create(Folder.HOLDS_MESSAGES);
                Message[] msgs = new Message[1];
                msgs[0] = msg;
                folder.appendMessages(msgs);
                System.out.println("Mail was recorded successfully.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
