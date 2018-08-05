    public String execute(HttpServletRequest request, HttpServletResponse response, User user, String parameter) throws Exception {
        long resourceId = ServletRequestUtils.getLongParameter(request, "resourceId", 0L);
        BinaryAttribute binaryAttribute = resourceManager.readAttribute(resourceId, parameter, user);
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
    }
