    public ValueAndClass returnValueAndClass(ReturnValueAndClassContext inRvacc) throws PFException {
        WorkflowComponentsForReturnableScriptTag wcfrt = ScriptTagHelper.createWorkflowComponentsForReturnableScriptTag(inRvacc);
        try {
            InternetAddress to[] = InternetAddress.parse(this.toTc.getValueAsString(), false);
            InternetAddress cc[] = InternetAddress.parse(this.ccTc.getValueAsString(), false);
            InternetAddress from[] = InternetAddress.parse(this.fromTc.getValueAsString(), false);
            Properties props = new Properties();
            props.put("mail.smtp.host", this.hostTc.getValueAsString());
            Session session = Session.getInstance(props, null);
            MimeMessage message = new MimeMessage(session);
            message.setSentDate(new Date());
            message.setSubject(this.subjectTc.getValueAsString());
            message.setRecipients(RecipientType.TO, to);
            message.setRecipients(RecipientType.CC, cc);
            message.addFrom(from);
            String messageText = "";
            for (int i = 0; i < this.messageTc.getValueLength(); i++) {
                messageText = messageText + this.messageTc.getValueAsString(i);
            }
            Multipart multipart = new MimeMultipart();
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(messageText);
            multipart.addBodyPart(messageBodyPart);
            for (int i = 0; i < this.attachmentTc.getValueLength(); i++) {
                String fileName = this.attachmentTc.getValueAsString(i);
                File file = new File(fileName);
                messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(fileName);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(file.getName());
                multipart.addBodyPart(messageBodyPart);
            }
            message.setContent(multipart);
            Transport.send(message);
            return ValueAndClassFactory.newValueAndClass(null, Void.TYPE);
        } catch (AddressException ae) {
            throw new PFException(ae);
        } catch (MessagingException me) {
            throw new PFException(me);
        }
    }
