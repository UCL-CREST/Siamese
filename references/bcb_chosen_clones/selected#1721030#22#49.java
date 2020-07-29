    @Override
    protected ActionForward executeAction(ActionMapping mapping, ActionForm form, User user, HttpServletRequest request, HttpServletResponse response) throws Exception {
        long resourceId = ServletRequestUtils.getLongParameter(request, "resourceId", 0L);
        String attributeIdentifier = request.getParameter("identifier");
        if (resourceId != 0L && StringUtils.hasText(attributeIdentifier)) {
            try {
                BinaryAttribute binaryAttribute = resourceManager.readAttribute(resourceId, attributeIdentifier, user);
                response.addHeader("Content-Disposition", "attachment; filename=\"" + binaryAttribute.getName() + '"');
                String contentType = binaryAttribute.getContentType();
                if (contentType != null) {
                    if ("application/x-zip-compressed".equalsIgnoreCase(contentType)) {
                        response.setContentType("application/octet-stream");
                    } else {
                        response.setContentType(contentType);
                    }
                } else {
                    response.setContentType("application/octet-stream");
                }
                IOUtils.copy(binaryAttribute.getInputStream(), response.getOutputStream());
                return null;
            } catch (DataRetrievalFailureException e) {
                addGlobalError(request, "errors.notFound");
            } catch (Exception e) {
                addGlobalError(request, e);
            }
        }
        return mapping.getInputForward();
    }
