    public static void sendHTML(Map<String, String> recipients, String object, MimeMultipart mime, String from) {
        Properties props = getRealSMTPServerProperties();
        if (props != null) {
            props.put("mail.from", from);
            Session session = Session.getInstance(props, null);
            try {
                MimeMessage msg = new MimeMessage(session);
                msg.setFrom();
                List<Address> adresses = new ArrayList<Address>();
                for (Entry<String, String> recipient : recipients.entrySet()) {
                    if (recipient.getValue() == null) {
                        adresses.add(new InternetAddress(recipient.getKey()));
                    } else {
                        adresses.add(new InternetAddress(recipient.getKey(), recipient.getValue()));
                    }
                }
                msg.setRecipients(Message.RecipientType.TO, adresses.toArray(new Address[0]));
                msg.setSubject(object);
                msg.setSentDate(new Date());
                msg.setContent(mime);
                Transport.send(msg);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
    }
