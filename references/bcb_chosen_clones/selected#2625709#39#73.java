    public ActionForward sendMail(ActionMapping mapping, ActionForm actForm, HttpServletRequest request, HttpServletResponse response) {
        ActionMessages errors = new ActionMessages();
        try {
            SendMailForm form = (SendMailForm) actForm;
            String senderEmail = form.getSenderEmail();
            String body = form.getBody();
            Integer mProposalId = (Integer) request.getSession().getAttribute(Constants.PROPOSAL_ID);
            ProposalFacadeLocal proposalFacade = ProposalFacadeUtil.getLocalHome().create();
            ProposalLightValue proposal = proposalFacade.findByPrimaryKey(mProposalId);
            String host = mProp.getProperty("mail.smtp.host");
            if (host == null) host = "localhost";
            String from = mProp.getProperty("mail.from");
            if (from == null) from = "ispyb@embl-grenoble.fr";
            String to = mProp.getProperty("mail.to");
            if (to == null) to = "root@localhost";
            String cc = mProp.getProperty("mail.cc");
            if (cc == null) cc = "root@localhost";
            Properties props = System.getProperties();
            props.put("mail.smtp.host", host);
            Session session = Session.getDefaultInstance(props, null);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.addRecipient(Message.RecipientType.CC, new InternetAddress(senderEmail));
            message.setSubject("[ISPyB Feedback] " + proposal.getCode() + proposal.getNumber());
            message.setText(senderEmail + "\r\r" + body);
            Transport.send(message);
        } catch (Exception e) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.detail", e.toString()));
            ClientLogger.getInstance().error(e.toString());
            saveErrors(request, errors);
            return mapping.findForward("error");
        }
        return mapping.findForward("HelpPage");
    }
