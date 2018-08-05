    public static boolean sendMail(String server, String toAddress, String fromName, String fromAddress, String ccAddress, String bccAddress, String subject, String message) {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", server);
        Session session = Session.getDefaultInstance(properties, null);
        Address ccInternetAddress = null, bccInternetAddress = null;
        try {
            MimeMessage msg = new MimeMessage(session);
            int ii;
            Address toInternetAddress = new InternetAddress(toAddress);
            msg.setRecipients(Message.RecipientType.TO, new Address[] { toInternetAddress });
            if (ccAddress != null && !ccAddress.equals("")) {
                ccInternetAddress = new InternetAddress(ccAddress);
                msg.setRecipients(Message.RecipientType.CC, new Address[] { ccInternetAddress });
            }
            if (bccAddress != null && !bccAddress.equals("")) {
                bccInternetAddress = new InternetAddress(bccAddress);
                msg.setRecipients(Message.RecipientType.BCC, new Address[] { bccInternetAddress });
            }
            MimeBodyPart textPart = new MimeBodyPart();
            msg.setFrom(new InternetAddress(fromAddress, fromName));
            msg.setSubject(subject);
            msg.setText(message);
            Transport.send(msg);
            return true;
        } catch (UnsupportedEncodingException uee) {
            System.err.println("U:" + uee);
            return false;
        } catch (AddressException ae) {
            System.err.println("A:" + ae);
            return false;
        } catch (MessagingException me) {
            System.err.println("M:" + me);
            return false;
        }
    }
