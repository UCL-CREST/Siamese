    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        Session session = HibernateUtil.getInstance().getSession();
        response.setBufferSize(65536);
        ServletOutputStream outStream = response.getOutputStream();
        File file = null;
        FileData fileData = null;
        try {
            String fileParameter = request.getParameter("file");
            String disposition = request.getParameter("disposition");
            if (fileParameter == null || fileParameter.equals("")) {
                String pi = request.getPathInfo();
                int lastSlashIndex = pi.lastIndexOf("/") + 1;
                fileParameter = pi.substring(lastSlashIndex, pi.indexOf("_", pi.lastIndexOf("/")));
            }
            if (fileParameter == null || fileParameter.equals("")) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.flushBuffer();
                Logger.log("file parameter not specified");
                return;
            }
            if (disposition == null || disposition.equals("")) {
                String pi = request.getPathInfo();
                String filename = pi.substring(pi.lastIndexOf("/") + 1);
                int underscoreIndex = filename.indexOf("_") + 1;
                disposition = filename.substring(underscoreIndex, filename.indexOf("_", underscoreIndex));
            }
            file = (File) session.load(File.class, new Long(fileParameter));
            Logger.log("Content requested=" + file.getName() + ":" + fileParameter + " Referral: " + request.getParameter("referer"));
            long ifModifiedSince = request.getDateHeader("If-Modified-Since");
            long fileDate = file.getLastModifiedDate() - (file.getLastModifiedDate() % 1000);
            if (fileDate <= ifModifiedSince) {
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                if ("attachment".equals(disposition)) {
                    response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
                } else {
                    response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
                }
                response.setContentType(file.getContentType());
                response.setHeader("Content-Description", file.getName());
                response.setDateHeader("Last-Modified", file.getLastModifiedDate());
                response.setDateHeader("Expires", System.currentTimeMillis() + 31536000000L);
                response.setContentLength((int) file.getSize());
                response.flushBuffer();
                Logger.log("Conditional GET: " + file.getName());
                return;
            }
            User authUser = baseService.getAuthenticatedUser(session, request, response);
            if (!SecurityHelper.doesUserHavePermission(session, authUser, file, Permission.PERM.READ)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.flushBuffer();
                Logger.log("Forbidden content requested: " + fileParameter);
                return;
            }
            String contentType = file.getContentType();
            response.setContentType(contentType);
            if ("attachment".equals(disposition)) {
                response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
            } else {
                response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
            }
            String name = file.getName();
            response.setHeader("Content-Description", name);
            response.setDateHeader("Last-Modified", file.getLastModifiedDate());
            response.setDateHeader("Expires", System.currentTimeMillis() + 31536000000L);
            response.setContentLength((int) file.getSize());
            java.io.File possibleDataFile = new java.io.File(BaseSystem.getTempDir() + file.getNameOnDisk());
            if (possibleDataFile.exists()) {
                Logger.log("File exists in " + BaseSystem.getTempDir() + " pulling " + possibleDataFile.getName());
                FileInputStream fileInputStream = new FileInputStream(possibleDataFile);
                try {
                    IOUtils.copy(fileInputStream, outStream);
                } finally {
                    try {
                        fileInputStream.close();
                    } catch (Throwable t) {
                    }
                }
            } else {
                List<FileData> fileDataList = HibernateUtil.getInstance().executeQuery(session, "from " + FileData.class.getSimpleName() + " where permissibleObject.id = " + file.getId());
                if (fileDataList.size() == 0) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    Logger.log("Requested content not found: " + fileParameter);
                    response.flushBuffer();
                    return;
                }
                fileData = (FileData) fileDataList.get(0);
                FileOutputStream fileOutputStream = null;
                try {
                    java.io.File tmpDir = new java.io.File(BaseSystem.getTempDir());
                    tmpDir.mkdirs();
                    fileOutputStream = new FileOutputStream(possibleDataFile);
                    IOUtils.write(fileData.getData(), fileOutputStream);
                } catch (Throwable t) {
                    Logger.log(t);
                } finally {
                    try {
                        fileOutputStream.close();
                    } catch (Throwable t) {
                    }
                }
                IOUtils.write(fileData.getData(), outStream);
            }
        } catch (Throwable t) {
            Logger.log(t);
            try {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.flushBuffer();
            } catch (Throwable tt) {
            }
            try {
                response.reset();
                response.resetBuffer();
            } catch (Throwable tt) {
            }
        } finally {
            file = null;
            fileData = null;
            try {
                outStream.flush();
            } catch (Throwable t) {
            }
            try {
                outStream.close();
            } catch (Throwable t) {
            }
            try {
                session.close();
            } catch (Throwable t) {
            }
        }
    }
