    public static boolean sendMail(String server, String fromAddress, String fromName, String replyToAddress, String replyToName, String[] toAddresses, String subject, String message, File[] attatchedFiles) {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", server);
        Session session = Session.getDefaultInstance(properties, null);
        try {
            MimeMessage msg = new MimeMessage(session);
            int numToAddress = toAddresses.length;
            int numFiles = attatchedFiles.length;
            int i;
            Address[] toInternetAddresses = new InternetAddress[numToAddress];
            Address[] replyToAddresses = { new InternetAddress(replyToAddress, replyToName) };
            MimeBodyPart textPart = new MimeBodyPart();
            MimeBodyPart[] fileParts = new MimeBodyPart[numFiles];
            MimeMultipart allTheParts = new MimeMultipart();
            msg.setFrom(new InternetAddress(fromAddress, fromName));
            msg.setReplyTo(replyToAddresses);
            msg.setSubject(subject);
            for (i = 0; i < numToAddress; i++) {
                toInternetAddresses[i] = new InternetAddress(toAddresses[i]);
            }
            msg.setRecipients(Message.RecipientType.TO, toInternetAddresses);
            if (numFiles > 0) {
                textPart.setText(message);
                allTheParts.addBodyPart(textPart);
                msg.setText("This message is in MIME format. Since your mail reader does not understand this format, some or all of this message may not be legible.");
                for (i = 0; i < numFiles; i++) {
                    if (attatchedFiles[i].exists()) {
                        fileParts[i] = new MimeBodyPart();
                        fileParts[i].setFileName(attatchedFiles[i].getName());
                        fileParts[i].setDataHandler(new DataHandler(new FileDataSource(attatchedFiles[i])));
                        allTheParts.addBodyPart(fileParts[i]);
                    }
                }
                msg.setContent(allTheParts);
            } else msg.setText(message);
            Transport.send(msg);
            return true;
        } catch (UnsupportedEncodingException uee) {
            return false;
        } catch (AddressException ae) {
            return false;
        } catch (MessagingException me) {
            return false;
        }
    }
