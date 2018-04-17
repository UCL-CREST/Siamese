    public String postEvent(EventDocument eventDoc, Map attachments) {
        if (eventDoc == null || eventDoc.getEvent() == null) return null;
        if (queue == null) {
            sendEvent(eventDoc, attachments);
            return eventDoc.getEvent().getEventId();
        }
        if (attachments != null) {
            Iterator iter = attachments.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                if (entry.getValue() instanceof DataHandler) {
                    File file = new File(attachmentStorge + "/" + GuidUtil.generate() + entry.getKey());
                    try {
                        IOUtils.copy(((DataHandler) entry.getValue()).getInputStream(), new FileOutputStream(file));
                        entry.setValue(file);
                    } catch (IOException err) {
                        err.printStackTrace();
                    }
                }
            }
        }
        InternalEventObject eventObj = new InternalEventObject();
        eventObj.setEventDocument(eventDoc);
        eventObj.setAttachments(attachments);
        eventObj.setSessionContext(SessionContextUtil.getCurrentContext());
        eventDoc.getEvent().setEventId(GuidUtil.generate());
        getQueue().post(eventObj);
        return eventDoc.getEvent().getEventId();
    }
