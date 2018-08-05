    private void compressMessageAttachments(MimeMessage message) throws MessagingException, IOException {
        Renderable rm = null;
        try {
            if (message.getContentType().startsWith("text/plain")) {
                rm = new RenderablePlainText(message);
            } else {
                rm = new RenderableMessage(message);
            }
        } catch (MessagingException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        int attachmentCount = rm.getAttachmentCount();
        if (attachmentCount > 0) {
            File tempOutFile = File.createTempFile("Msg-Consolidated-Attachments", "zip");
            ZipOutputStream zipFile = new ZipOutputStream(new FileOutputStream(tempOutFile));
            for (int attachment = 0; attachment < attachmentCount; attachment++) {
                Attachment a = rm.getAttachment(attachment);
                ZipEntry entry = new ZipEntry(a.getFilename());
                zipFile.putNextEntry(entry);
                zipFile.write(a.getContent());
                zipFile.closeEntry();
            }
            zipFile.close();
            MimeMultipart mp = new MimeMultipart();
            MimeBodyPart bp2 = new MimeBodyPart();
            bp2.setFileName("Msg-Consolidated-Attachments.zip");
            bp2.attachFile(tempOutFile);
            mp.addBodyPart(bp2);
            message.setContent(mp);
        }
    }
