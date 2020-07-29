    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionMessages errors = new ActionMessages();
        if (!hasPermission(UserUtilities.PERMISSION_USER_ADMIN, request, response)) {
            return mapping.findForward("unauthorized");
        }
        try {
            IssueService issueService = getITrackerServices().getIssueService();
            List<IssueAttachment> attachments = issueService.getAllIssueAttachments();
            if (attachments.size() > 0) {
                response.setHeader("Content-Disposition", "attachment; filename=\"ITracker_attachments.zip\"");
                ServletOutputStream out = response.getOutputStream();
                ZipOutputStream zipOut = new ZipOutputStream(out);
                try {
                    for (int i = 0; i < attachments.size(); i++) {
                        log.debug("Attempting export for: " + attachments.get(i));
                        byte[] attachmentData = issueService.getIssueAttachmentData(attachments.get(i).getId());
                        if (attachmentData.length > 0) {
                            ZipEntry zipEntry = new ZipEntry(attachments.get(i).getFileName());
                            zipEntry.setSize(attachmentData.length);
                            zipEntry.setTime(attachments.get(i).getLastModifiedDate().getTime());
                            zipOut.putNextEntry(zipEntry);
                            zipOut.write(attachmentData, 0, attachmentData.length);
                            zipOut.closeEntry();
                        }
                    }
                    zipOut.close();
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    log.error("Exception while exporting attachments.", e);
                }
                return null;
            }
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("itracker.web.error.noattachments"));
        } catch (Exception e) {
            log.error("Exception while exporting attachments.", e);
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("itracker.web.error.system"));
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
        }
        return mapping.findForward("error");
    }
