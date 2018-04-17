    private void sendMail(Vector myData, InternetAddress mySender) {
        if (!myData.isEmpty()) {
            Properties myProp = System.getProperties();
            Session mySession = Session.getInstance(myProp, null);
            mySession.setDebug(DEBUG);
            myProp.put("mail.smtp.host", MAILHOST);
            Message myMessage = new MimeMessage(mySession);
            try {
                myMessage.setFrom(new InternetAddress(FROM));
                myMessage.setRecipient(Message.RecipientType.TO, mySender);
                myMessage.setHeader("X-Mailer", MAILER);
                myMessage.setSubject(SUBJECT);
                myMessage.setSentDate(new Date());
            } catch (MessagingException e) {
                System.out.println(e.getMessage());
                System.out.println("Check from, recipient, mail, subject and date. There's a problem.");
            }
            String myMessageBody = "";
            for (int i = 0; i < myData.size(); i++) {
                Vector myVec = ((Vector) myData.get(i));
                for (int j = 0; j < myVec.size(); j++) {
                    MRData data = ((MRData) myVec.get(j));
                    myMessageBody = myMessageBody.concat(data.getUser() + ":" + data.getAddress() + ":" + data.getScore() + ":" + data.getCount() + "\n");
                }
            }
            if (DEBUG) System.out.println("MRMAIL BodyText: " + myMessageBody);
            if (myMessageBody != null) {
                try {
                    myMessage.setText(myMessageBody);
                } catch (MessagingException e) {
                    System.out.println(e.getMessage());
                    System.out.println("The Messagebody is not valid");
                }
                try {
                    Transport.send(myMessage);
                } catch (MessagingException e) {
                    System.out.println(e.getMessage());
                    System.out.println("Can't send the mail.");
                }
            }
        } else {
            if (DEBUG) System.out.println("Result is empty. Maybe only setValues? Not sending an email");
        }
    }
