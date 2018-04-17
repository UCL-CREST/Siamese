        public FormSubmissionResult submit() {
            StringBuilder sb = new StringBuilder();
            for (FormField field : form.getFields()) {
                String label = field.getLabel();
                if (label != null) {
                    sb.append(label).append('\n');
                    sb.append(field.getValue()).append("\n\n");
                }
            }
            HttpServletRequest request = Context.getRequest();
            try {
                InternetAddress senderAddress = new InternetAddress(form.getSender());
                InternetAddress recipientAddress = new InternetAddress(form.getDestination());
                Session mailSession = WebUtils.getMailSession();
                MimeMessage outMsg = new MimeMessage(mailSession);
                outMsg.setFrom(senderAddress);
                outMsg.addRecipient(Message.RecipientType.TO, recipientAddress);
                outMsg.setSubject("Message from " + request.getServerName());
                outMsg.setHeader("Content-Transfer-Encoding", "8bit");
                outMsg.setHeader("X-MeshCMS-Log", "Sent from " + request.getRemoteAddr() + " at " + new Date() + " using page /" + rc.getPagePath());
                outMsg.setText(sb.toString());
                Transport.send(outMsg);
            } catch (Exception ex) {
                Context.log(ex);
                return FormSubmissionResult.getDefaultError(form.getId());
            }
            return new FormSubmissionResult(form.getId(), successMessage, false, null);
        }
