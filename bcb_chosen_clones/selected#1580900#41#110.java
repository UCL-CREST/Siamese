    @Override
    protected void handlePost(PageContext pageContext, Template template, VelocityContext templateContext) throws ServletException, IOException {
        IDAOSession f;
        try {
            DAOFactory df = (DAOFactory) FactoryRegistrar.getFactory(DAOFactory.class);
            f = df.getInstance();
        } catch (FactoryException e) {
            throw new ServletException("dao init error", e);
        }
        String assignmentString = pageContext.getParameter("assignment");
        if (assignmentString == null) {
            throw new ServletException("No assignment parameter given");
        }
        Long assignmentId = Long.valueOf(pageContext.getParameter("assignment"));
        boolean oneDirectory = (pageContext.getParameter("one_directory") != null);
        boolean activeOnly = (pageContext.getParameter("active_only") != null);
        try {
            f.beginTransaction();
            IStaffInterfaceQueriesDAO staffInterfaceQueriesDao = f.getStaffInterfaceQueriesDAOInstance();
            IAssignmentDAO assignmentDao = f.getAssignmentDAOInstance();
            Assignment assignment = assignmentDao.retrievePersistentEntity(assignmentId);
            if (!staffInterfaceQueriesDao.isStaffModuleAccessAllowed(pageContext.getSession().getPersonBinding().getId(), assignment.getModuleId())) {
                f.abortTransaction();
                throw new ServletException("permission denied");
            }
            ISubmissionDAO submissionDao = f.getSubmissionDAOInstance();
            Submission exampleSubmission = new Submission();
            exampleSubmission.setAssignmentId(assignmentId);
            if (activeOnly) {
                exampleSubmission.setActive(true);
            }
            Collection<Submission> submissionsToDownload = submissionDao.findPersistentEntitiesByExample(exampleSubmission);
            pageContext.log(Level.DEBUG, "Downloading " + submissionsToDownload.size() + " submission(s)");
            try {
                IResourceDAO resourceDao = f.getResourceDAOInstance();
                OutputStream outputStream = pageContext.performManualSendFile("application/zip", "assignment-" + assignment.getId() + "-submissions.zip");
                ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
                for (Submission submission : submissionsToDownload) {
                    InputStream resourceStream = resourceDao.openInputStream(submission.getResourceId());
                    ZipInputStream zipResourceStream = new ZipInputStream(resourceStream);
                    ZipEntry currentZipEntry;
                    while ((currentZipEntry = zipResourceStream.getNextEntry()) != null) {
                        String fileName = new File(currentZipEntry.getName()).getName();
                        if (oneDirectory) {
                            fileName = "Assignment " + assignment.getId() + "_" + submission.getResourceSubdirectory() + "_Submission " + submission.getId() + "_" + fileName;
                        } else {
                            fileName = "Assignment " + assignment.getId() + "/" + submission.getResourceSubdirectory() + "/Submission " + submission.getId() + "/" + fileName;
                        }
                        zipOutputStream.putNextEntry(new ZipEntry(fileName));
                        byte buffer[] = new byte[1024];
                        int nread = -1;
                        long total = 0;
                        while ((nread = zipResourceStream.read(buffer)) != -1) {
                            total += nread;
                            zipOutputStream.write(buffer, 0, nread);
                        }
                    }
                }
                zipOutputStream.finish();
                zipOutputStream.flush();
                outputStream.flush();
            } catch (Exception e) {
                pageContext.log(Level.ERROR, e);
            }
            f.endTransaction();
        } catch (DAOException e) {
            f.abortTransaction();
            throw new ServletException("dao exception", e);
        }
    }
