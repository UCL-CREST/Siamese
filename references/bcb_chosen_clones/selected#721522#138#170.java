        public void run() {
            try {
                Message msg = new MimeMessage(session);
                msg.setFrom(new InternetAddress(blog.getFirstEmailAddress(), blog.getName()));
                Collection internetAddresses = new HashSet();
                Iterator it = to.iterator();
                while (it.hasNext()) {
                    internetAddresses.add(new InternetAddress(it.next().toString()));
                }
                msg.addRecipients(Message.RecipientType.TO, (InternetAddress[]) internetAddresses.toArray(new InternetAddress[] {}));
                internetAddresses = new HashSet();
                it = cc.iterator();
                while (it.hasNext()) {
                    internetAddresses.add(new InternetAddress(it.next().toString()));
                }
                msg.addRecipients(Message.RecipientType.CC, (InternetAddress[]) internetAddresses.toArray(new InternetAddress[] {}));
                internetAddresses = new HashSet();
                it = bcc.iterator();
                while (it.hasNext()) {
                    internetAddresses.add(new InternetAddress(it.next().toString()));
                }
                msg.addRecipients(Message.RecipientType.BCC, (InternetAddress[]) internetAddresses.toArray(new InternetAddress[] {}));
                msg.setSubject(subject);
                msg.setSentDate(new Date());
                msg.setContent(message, "text/html");
                log.debug("From : " + blog.getName() + " (" + blog.getFirstEmailAddress() + ")");
                log.debug("Subject : " + subject);
                log.debug("Message : " + message);
                Transport.send(msg);
            } catch (Exception e) {
                log.error("Notification e-mail could not be sent", e);
            }
        }
