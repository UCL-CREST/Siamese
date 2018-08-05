    public static void main(String[] args) {
        if (args.length != 4) {
            usage();
            System.exit(1);
        }
        System.out.println();
        String to = args[0];
        String from = args[1];
        String host = args[2];
        boolean debug = Boolean.valueOf(args[3]).booleanValue();
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        if (debug) props.put("mail.debug", args[3]);
        Session session = Session.getInstance(props, null);
        session.setDebug(debug);
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));
            InternetAddress[] address = { new InternetAddress(args[0]) };
            msg.setRecipients(Message.RecipientType.TO, address);
            msg.setSubject("JavaMail APIs Test");
            msg.setSentDate(new Date());
            msg.setText(msgText);
            Transport.send(msg);
        } catch (MessagingException mex) {
            System.out.println("\n--Exception handling in msgsendsample.java");
            mex.printStackTrace();
            System.out.println();
            Exception ex = mex;
            do {
                if (ex instanceof SendFailedException) {
                    SendFailedException sfex = (SendFailedException) ex;
                    Address[] invalid = sfex.getInvalidAddresses();
                    if (invalid != null) {
                        System.out.println("    ** Invalid Addresses");
                        if (invalid != null) {
                            for (int i = 0; i < invalid.length; i++) System.out.println("         " + invalid[i]);
                        }
                    }
                    Address[] validUnsent = sfex.getValidUnsentAddresses();
                    if (validUnsent != null) {
                        System.out.println("    ** ValidUnsent Addresses");
                        if (validUnsent != null) {
                            for (int i = 0; i < validUnsent.length; i++) System.out.println("         " + validUnsent[i]);
                        }
                    }
                    Address[] validSent = sfex.getValidSentAddresses();
                    if (validSent != null) {
                        System.out.println("    ** ValidSent Addresses");
                        if (validSent != null) {
                            for (int i = 0; i < validSent.length; i++) System.out.println("         " + validSent[i]);
                        }
                    }
                }
                System.out.println();
                if (ex instanceof MessagingException) ex = ((MessagingException) ex).getNextException(); else ex = null;
            } while (ex != null);
        }
    }
