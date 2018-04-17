    public ActionForward severalContentsExportation(ActionMapping mapping, ActionForm form, PortletConfig portletConfig, PortletRequest request, PortletResponse response) throws CMSRuntimeException {
        DynaActionForm dform = (DynaActionForm) form;
        String[] listContentsIDs = (String[]) dform.get("listContentsIDs");
        String workspace = new ScribeRequest(request).calculateCurrentWorkspace();
        ContentManager contentManager = ManagerRegistry.getContentManager();
        ActionMessages msgs = new ActionMessages();
        ActionMessages errors = new ActionMessages();
        if ((listContentsIDs.length < 1)) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("content_admin.none.selected"));
            saveErrors(request, errors);
            return mapping.findForward("list");
        }
        try {
            File zipOutFilename = new File(EXPORTED_CONTENTS_PATH, CONTENTS_ZIP_PREFIX + Long.toString(System.currentTimeMillis()) + ZIP_FILE_EXTENSION);
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipOutFilename));
            for (String contentId : listContentsIDs) {
                try {
                    log.info("Exporting content:" + contentId);
                    out.putNextEntry(new ZipEntry(contentId));
                    PortletContextImpl portletContextImpl = ((PortletContextImpl) portletConfig.getPortletContext());
                    ServletContext servletContext = portletContextImpl.getServletContext();
                    byte[] contentByteArray = contentManager.exportContentDataByContentId(1, contentId, workspace, servletContext);
                    out.write(contentByteArray);
                } catch (RepositoryException e1) {
                    log.error("Error accesing repository");
                    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("content_admin.export.unexpected"));
                    saveErrors(request, errors);
                    return mapping.findForward("list");
                } catch (IOException e) {
                    log.error("Error managing file operations");
                    errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("content_admin.export.unexpected"));
                    saveErrors(request, errors);
                    return mapping.findForward("list");
                }
            }
            out.close();
        } catch (IOException e) {
            log.error("Error managing file operations");
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("content_admin.export.unexpected"));
            saveErrors(request, errors);
            return mapping.findForward("list");
        }
        if (listContentsIDs.length > 0) {
            msgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("content_admin.export.success", String.valueOf(listContentsIDs.length)));
        }
        saveMessages(request, msgs);
        saveErrors(request, errors);
        return mapping.findForward("list");
    }
