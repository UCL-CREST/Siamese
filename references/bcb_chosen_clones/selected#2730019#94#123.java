    public static void inviaAppuntamentoPerCalendarioTramiteEmail(int wrID) throws Exception {
        try {
            String from = "noreply@jmagazzino.org";
            String to = "xx@xx.com";
            java.util.Properties prop = new java.util.Properties();
            prop.put("mail.smtp.host", "mailhost");
            javax.mail.Session session = javax.mail.Session.getDefaultInstance(prop, null);
            javax.mail.internet.MimeMessage message = new javax.mail.internet.MimeMessage(session);
            message.addHeaderLine("method=REQUEST");
            message.addHeaderLine("charset=UTF-8");
            message.addHeaderLine("component=VEVENT");
            message.setFrom(new javax.mail.internet.InternetAddress(from));
            message.addRecipient(javax.mail.Message.RecipientType.TO, new javax.mail.internet.InternetAddress(to));
            message.setSubject("jMagazzino - Invio appuntamento WR - tramite javaMail");
            StringBuffer sb = new StringBuffer();
            StringBuffer buffer = sb.append("BEGIN:VCALENDAR\n" + "PRODID:-//Microsoft Corporation//Outlook 9.0 MIMEDIR//EN\n" + "VERSION:2.0\n" + "METHOD:REQUEST\n" + "BEGIN:VEVENT\n" + "ATTENDEE;ROLE=REQ-PARTICIPANT;RSVP=TRUE:MAILTO:xx@xx.com\n" + "ORGANIZER:MAILTO:xx@xx.com\n" + "DTSTART:20051208T053000Z\n" + "DTEND:20051208T060000Z\n" + "LOCATION:Conference room\n" + "TRANSP:OPAQUE\n" + "SEQUENCE:0\n" + "UID:WR-APPUNTAMENTO-ID-" + Integer.toString(wrID) + "\n" + "DTSTAMP:20051206T120102Z\n" + "CATEGORIES:Meeting\n" + "DESCRIPTION:This the description of the meeting.\n\n" + "SUMMARY:Richiesta appuntamento WR\n" + "PRIORITY:5\n" + "CLASS:PUBLIC\n" + "BEGIN:VALARM\n" + "TRIGGER:PT1440M\n" + "ACTION:DISPLAY\n" + "DESCRIPTION:Reminder\n" + "END:VALARM\n" + "END:VEVENT\n" + "END:VCALENDAR");
            javax.mail.BodyPart messageBodyPart = new javax.mail.internet.MimeBodyPart();
            messageBodyPart.setHeader("Content-Class", "urn:content-classes:calendarmessage");
            messageBodyPart.setHeader("Content-ID", "calendar_message");
            messageBodyPart.setDataHandler(new javax.activation.DataHandler(new javax.mail.util.ByteArrayDataSource(buffer.toString(), "text/calendar")));
            javax.mail.Multipart multipart = new javax.mail.internet.MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);
            javax.mail.Transport.send(message);
        } catch (javax.mail.MessagingException me) {
            me.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
