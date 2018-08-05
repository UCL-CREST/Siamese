    public void doAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String attachmentName = request.getParameter("attachment");
        String virtualWiki = getVirtualWiki(request);
        File uploadPath = getEnvironment().uploadPath(virtualWiki, attachmentName);
        response.reset();
        response.setHeader("Content-Disposition", getEnvironment().getStringSetting(Environment.PROPERTY_ATTACHMENT_TYPE) + ";filename=" + attachmentName + ";");
        int dotIndex = attachmentName.indexOf('.');
        if (dotIndex >= 0 && dotIndex < attachmentName.length() - 1) {
            String extension = attachmentName.substring(attachmentName.lastIndexOf('.') + 1);
            logger.fine("Extension: " + extension);
            String mimetype = (String) getMimeByExtension().get(extension.toLowerCase());
            logger.fine("MIME: " + mimetype);
            if (mimetype != null) {
                logger.fine("Setting content type to: " + mimetype);
                response.setContentType(mimetype);
            }
        }
        FileInputStream in = null;
        ServletOutputStream out = null;
        try {
            in = new FileInputStream(uploadPath);
            out = response.getOutputStream();
            IOUtils.copy(in, out);
            out.flush();
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }
    }
