    public void send() throws Exception {
        try {
            log.debug("#################################################");
            this.message = this.setMessage();
            Collection cEmails = new ArrayList();
            log.debug("TAM EMAILS: " + this.emails.size());
            if (this.emails.size() > 0) {
                Iterator iMails = this.emails.iterator();
                while (iMails.hasNext()) {
                    String email = (String) iMails.next();
                    log.debug("EMAIL: " + email);
                    if (!cEmails.contains(email)) {
                        cEmails.add(email);
                    }
                }
            }
            log.debug("TAM CEMAIL: " + cEmails.size());
            if (cEmails.size() > 0) {
                InternetAddress toAddress[] = new InternetAddress[cEmails.size()];
                String emails = "";
                Iterator iEmails = cEmails.iterator();
                int i = 0;
                while (iEmails.hasNext()) {
                    String sEmail = (String) iEmails.next();
                    try {
                        log.debug("SEMAIL: " + sEmail);
                        toAddress[i] = new InternetAddress(sEmail);
                        emails += "[" + sEmail + "]";
                        i++;
                    } catch (AddressException ex) {
                        log.warn("O e-mail [" + sEmail + "] n�o � v�lido.");
                    }
                }
                log.debug("TAM TOADRESS: " + toAddress.length);
                for (int j = 0; toAddress.length > j; j++) {
                    log.debug("Envio de e-mail para [" + cEmails.size() + "] usu�rios. " + emails);
                    InternetAddress fromAddress = new InternetAddress(this.sender);
                    Properties props = new Properties();
                    log.debug("SMTP: " + this.SMTPHost);
                    props.put("mail.smtp.host", this.SMTPHost);
                    MimeMessage email = new MimeMessage(Session.getInstance(props));
                    log.debug("EMAIL: " + email);
                    log.debug("FROMADRESS: " + fromAddress);
                    email.setFrom(fromAddress);
                    log.debug("j " + j);
                    log.debug("Adress: " + toAddress[j]);
                    email.setRecipient(this.recipientType, toAddress[j]);
                    log.debug("SUBJECT: " + this.subject);
                    email.setSubject(this.subject);
                    log.debug("MESSAGE: " + this.message);
                    email.setContent(this.message, "text/html");
                    Transport.send(email);
                }
            }
        } catch (MessagingException ex1) {
            log.error("Falha inesperada ao tentar enviar e-mail [" + this.subject + "]: " + ex1.getNextException(), ex1);
            log.error("Falha inesperada: " + ex1.getNextException().getMessage(), ex1.getNextException());
        }
        log.debug("#################################################");
    }
