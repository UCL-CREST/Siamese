    public void xtestGetThread() throws Exception {
        GMSearchOptions options = new GMSearchOptions();
        options.setFrom(loginInfo.getUsername() + "*");
        options.setSubject("message*");
        GMSearchResponse mail = client.getMail(options);
        for (Iterator it = mail.getThreadSnapshots().iterator(); it.hasNext(); ) {
            GMThreadSnapshot threadSnapshot = (GMThreadSnapshot) it.next();
            GMThread thread = client.getThread(threadSnapshot.getThreadID());
            log.info("Most Recent Thread: " + thread);
            for (Iterator iter = thread.getMessages().iterator(); iter.hasNext(); ) {
                GMMessage message = (GMMessage) iter.next();
                log.info("Message: " + message);
                Iterable<GMAttachment> attachments = message.getAttachments();
                for (Iterator iterator = attachments.iterator(); iterator.hasNext(); ) {
                    GMAttachment attachment = (GMAttachment) iterator.next();
                    String ext = FilenameUtils.getExtension(attachment.getFilename());
                    if (ext.trim().length() > 0) ext = "." + ext;
                    String base = FilenameUtils.getBaseName(attachment.getFilename());
                    File file = File.createTempFile(base, ext, new File(System.getProperty("user.home")));
                    log.info("Saving attachment: " + file.getPath());
                    InputStream attStream = client.getAttachmentAsStream(attachment.getId(), message.getMessageID());
                    IOUtils.copy(attStream, new FileOutputStream(file));
                    attStream.close();
                    assertEquals(file.length(), attachment.getSize());
                    log.info("Done. Successfully saved: " + file.getPath());
                    file.delete();
                }
            }
        }
    }
