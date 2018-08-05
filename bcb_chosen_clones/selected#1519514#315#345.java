    @RequestMapping(value = "/sendInboxResponse", method = RequestMethod.POST)
    @ResponseBody
    public String sendInboxResponseEmail(@RequestParam(required = true) Long inboxId) {
        try {
            Locale locale = this.getAvailableLanguages().get(0);
            Inbox messageRes = inboxService.getInbox(inboxId, locale);
            Inbox messageReq = null;
            if (messageRes != null) {
                messageReq = inboxService.getInbox(messageRes.getRelatedId(), locale);
            }
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);
            try {
                Message msg = new MimeMessage(session);
                msg.setFrom(new InternetAddress(new StringBuffer("admin").append(EMAIL_DOMAIN_SUFFIX).toString(), this.getMessage("suggestion.problem.response.from")));
                msg.addRecipient(Message.RecipientType.TO, new InternetAddress(messageReq.getFrom(), messageReq.getName()));
                msg.setSubject(this.getMessage("suggestion.problem.response.subject", messageReq.getId()));
                msg.setText(new StringBuffer(messageRes.getContent()).append("\n\n\n").append(this.getMessage("suggestion.problem.response.advice")).toString());
                Transport.send(msg);
            } catch (AddressException e) {
                LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
            } catch (MessagingException e) {
                LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
            }
            LOGGER.info("SENDING SEARCH RESULT TO EMAIL -> " + messageReq.getFrom());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
            return "common.error";
        }
        return "task.launched";
    }
