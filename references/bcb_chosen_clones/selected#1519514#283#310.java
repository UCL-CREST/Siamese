    @RequestMapping(value = "/sendSearchResult", method = RequestMethod.POST)
    @ResponseBody
    public String sendSearchResultEmail(@RequestParam(required = true) Long resultSearchEmailId) {
        try {
            ResultSearchEmail email = inboxService.getResultSearchEmail(resultSearchEmailId, Locale.getDefault());
            Properties props = new Properties();
            Session session = Session.getDefaultInstance(props, null);
            try {
                Message msg = new MimeMessage(session);
                msg.setFrom(new InternetAddress(new StringBuffer("info").append(EMAIL_DOMAIN_SUFFIX).toString(), this.getMessage("search.result.send.from")));
                msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email.getTo(), email.getToName()));
                msg.setSubject(this.getMessage("search.result.send.subject", email.getFromName()));
                msg.setText(this.getMessage("search.result.send.body", email.getResultLink()).toString());
                Transport.send(msg);
                email.setSent(Boolean.TRUE);
                inboxService.save(email, Locale.getDefault());
            } catch (AddressException e) {
                LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
            } catch (MessagingException e) {
                LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
            }
            LOGGER.info("SENDING SEARCH RESULT TO EMAIL -> " + email.getTo());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, StackTraceUtil.getStackTrace(e));
            return "common.error";
        }
        return "task.launched";
    }
