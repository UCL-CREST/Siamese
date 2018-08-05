    protected ActionForward doExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionMessages errors = new ActionMessages();
        try {
            boolean isMultipart = FileUpload.isMultipartContent(request);
            Store storeInstance = getStoreInstance(request);
            if (isMultipart) {
                Map fields = new HashMap();
                Vector files = new Vector();
                List items = diskFileUpload.parseRequest(request);
                Iterator iter = items.iterator();
                while (iter.hasNext()) {
                    FileItem item = (FileItem) iter.next();
                    if (item.isFormField()) {
                        fields.put(item.getFieldName(), item.getString());
                    } else {
                        if (!StringUtils.isBlank(item.getName())) {
                            ByteArrayOutputStream baos = null;
                            try {
                                baos = new ByteArrayOutputStream();
                                IOUtils.copy(item.getInputStream(), baos);
                                MailPartObj part = new MailPartObj();
                                part.setAttachent(baos.toByteArray());
                                part.setContentType(item.getContentType());
                                part.setName(item.getName());
                                part.setSize(item.getSize());
                                files.addElement(part);
                            } catch (Exception ex) {
                            } finally {
                                IOUtils.closeQuietly(baos);
                            }
                        }
                    }
                }
                if (files.size() > 0) {
                    storeInstance.send(files, 0, Charset.defaultCharset().displayName());
                }
            } else {
                errors.add("general", new ActionMessage(ExceptionCode.ERROR_MESSAGES_PREFIX + "mail.send", "The form is null"));
                request.setAttribute("exception", "The form is null");
                request.setAttribute("newLocation", null);
                doTrace(request, DLog.ERROR, getClass(), "The form is null");
            }
        } catch (Exception ex) {
            String errorMessage = ExceptionUtilities.parseMessage(ex);
            if (errorMessage == null) {
                errorMessage = "NullPointerException";
            }
            errors.add("general", new ActionMessage(ExceptionCode.ERROR_MESSAGES_PREFIX + "general", errorMessage));
            request.setAttribute("exception", errorMessage);
            doTrace(request, DLog.ERROR, getClass(), errorMessage);
        } finally {
        }
        if (errors.isEmpty()) {
            doTrace(request, DLog.INFO, getClass(), "OK");
            return mapping.findForward(Constants.ACTION_SUCCESS_FORWARD);
        } else {
            saveErrors(request, errors);
            return mapping.findForward(Constants.ACTION_FAIL_FORWARD);
        }
    }
