    @SuppressWarnings("unchecked")
    private void appendAttachments(final Part part) throws MessagingException, IOException {
        if (part.isMimeType("message/*")) {
            JcrMessage jcrMessage = new JcrMessage();
            Message attachedMessage = null;
            if (part.getContent() instanceof Message) {
                attachedMessage = (Message) part.getContent();
            } else {
                attachedMessage = new MStorMessage(null, (InputStream) part.getContent());
            }
            jcrMessage.setFlags(attachedMessage.getFlags());
            jcrMessage.setHeaders(attachedMessage.getAllHeaders());
            jcrMessage.setReceived(attachedMessage.getReceivedDate());
            jcrMessage.setExpunged(attachedMessage.isExpunged());
            jcrMessage.setMessage(attachedMessage);
            messages.add(jcrMessage);
        } else if (part.isMimeType("multipart/*")) {
            Multipart multi = (Multipart) part.getContent();
            for (int i = 0; i < multi.getCount(); i++) {
                appendAttachments(multi.getBodyPart(i));
            }
        } else if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition()) || StringUtils.isNotEmpty(part.getFileName())) {
            JcrFile attachment = new JcrFile();
            String name = null;
            if (StringUtils.isNotEmpty(part.getFileName())) {
                name = part.getFileName();
                for (JcrFile attach : attachments) {
                    if (attach.getName().equals(name)) {
                        return;
                    }
                }
            } else {
                String[] contentId = part.getHeader("Content-Id");
                if (contentId != null && contentId.length > 0) {
                    name = contentId[0];
                } else {
                    name = "attachment";
                }
            }
            int count = 0;
            for (JcrFile attach : attachments) {
                if (attach.getName().equals(name)) {
                    count++;
                }
            }
            if (count > 0) {
                name += "_" + count;
            }
            attachment.setName(name);
            ByteArrayOutputStream pout = new ByteArrayOutputStream();
            IOUtils.copy(part.getInputStream(), pout);
            attachment.setDataProvider(new JcrDataProviderImpl(TYPE.BYTES, pout.toByteArray()));
            attachment.setMimeType(part.getContentType());
            attachment.setLastModified(java.util.Calendar.getInstance());
            attachments.add(attachment);
        }
    }
