    private void doWork(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.debug("Downloading resource...");
        String contentTypeName = request.getParameter(CONTENT_TYPE_KEY);
        ContentType contentType = ContentType.valueOf(contentTypeName);
        logger.debug("  content type is: " + contentType);
        String filename = (String) Contexts.getSessionContext().get(FILENAME_KEY);
        logger.debug("  file name is: " + filename);
        byte[] downloadData = (byte[]) Contexts.getSessionContext().get(SESSION_KEY);
        logger.debug("  download data is: " + String.valueOf(downloadData));
        if (downloadData == null) {
            logger.error("download failed, data is null");
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } else {
            logger.debug(" writing to output stream... ");
            boolean compress = Boolean.valueOf(request.getParameter(COMPRESS_KEY));
            if (compress) {
                logger.debug(" zip flag setted -> using compression");
                response.setContentType(ContentType.ZIP.getRealContentType());
                response.setHeader("Content-Disposition", "attachment; filename=\"" + makeUTF8Filename(filename + ContentType.ZIP.getFileExtension()) + "\"");
                ZipOutputStream stream = new ZipOutputStream(response.getOutputStream());
                stream.putNextEntry(new ZipEntry(makeUTF8Filename(filename + contentType.getFileExtension())));
                stream.write(downloadData);
                stream.close();
            } else {
                response.setContentType(contentType.getRealContentType());
                response.setHeader("Content-Disposition", "attachment; filename=\"" + makeUTF8Filename(filename + contentType.getFileExtension()) + "\"");
                response.setContentLength(downloadData.length);
                response.getOutputStream().write(downloadData);
            }
            response.flushBuffer();
            response.getOutputStream().flush();
            response.getOutputStream().close();
            Contexts.getSessionContext().set(SESSION_KEY, null);
            Contexts.getSessionContext().set(FILENAME_KEY, null);
            logger.debug("Downloading resource...Ok");
        }
    }
