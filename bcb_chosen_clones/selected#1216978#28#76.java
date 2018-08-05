    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionErrors errors = new ActionErrors();
        if (!isLoggedIn(request, response)) {
            return mapping.findForward("login");
        }
        if (!hasPermission(UserUtilities.PERMISSION_USER_ADMIN, request, response)) {
            return mapping.findForward("unauthorized");
        }
        try {
            InitialContext ic = new InitialContext();
            Object ihRef = ic.lookup("java:comp/env/" + IssueHandler.JNDI_NAME);
            IssueHandlerHome ihHome = (IssueHandlerHome) PortableRemoteObject.narrow(ihRef, IssueHandlerHome.class);
            IssueHandler ih = ihHome.create();
            IssueAttachmentModel[] attachments = ih.getAllIssueAttachments();
            if (attachments.length > 0) {
                response.setHeader("Content-Disposition", "attachment; filename=\"ITracker_attachments.zip\"");
                ServletOutputStream out = response.getOutputStream();
                ZipOutputStream zipOut = new ZipOutputStream(out);
                try {
                    for (int i = 0; i < attachments.length; i++) {
                        Logger.logDebug("Attempting export for: " + attachments[i]);
                        byte[] attachmentData = ih.getIssueAttachmentData(attachments[i].getId());
                        if (attachmentData.length > 0) {
                            ZipEntry zipEntry = new ZipEntry(attachments[i].getFileName() + attachments[i].getFileExtension());
                            zipEntry.setSize(attachmentData.length);
                            zipEntry.setTime(attachments[i].getLastModifiedDate().getTime());
                            zipOut.putNextEntry(zipEntry);
                            zipOut.write(attachmentData, 0, attachmentData.length);
                            zipOut.closeEntry();
                        }
                    }
                    zipOut.close();
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    Logger.logError("Exception while exporting attachments.", e);
                }
                return null;
            }
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("itracker.web.error.noattachments"));
        } catch (Exception e) {
            Logger.logError("Exception while exporting attachments.", e);
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("itracker.web.error.system"));
        }
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
        }
        return mapping.findForward("error");
    }
