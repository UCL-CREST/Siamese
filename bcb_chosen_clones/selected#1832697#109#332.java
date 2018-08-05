    protected void doUpload(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession reqSession = request.getSession();
        ArrayList<File> uploadFiles = new ArrayList<File>();
        LOG.info("UploadServlet Upload request received");
        if (ServletFileUpload.isMultipartContent(request)) {
            LOG.debug("UploadServlet Received a multipart request.");
        } else {
            LOG.debug("UploadServlet Received a non-multipart request.");
        }
        String tempDirName = UUID.randomUUID().toString();
        File tempUploadDir = new File(adapter.getRootPath() + File.separator + "temp" + File.separator + tempDirName);
        tempUploadDir.getParentFile().mkdir();
        while (tempUploadDir.exists()) {
            tempDirName = UUID.randomUUID().toString();
            tempUploadDir = new File(adapter.getRootPath() + File.separator + "temp" + File.separator + tempDirName);
        }
        tempUploadDir.mkdir();
        File attachmentDir = (File) (reqSession.getAttribute("up2p:attachdir"));
        if (attachmentDir != null) {
            LOG.info("UploadServlet: Copying provided attachment to upload dir from: " + attachmentDir.getAbsolutePath());
            tempUploadDir.delete();
            attachmentDir.renameTo(tempUploadDir);
            reqSession.removeAttribute("up2p:attachdir");
        }
        LOG.info("UploadServlet: Using temporary directory: " + tempUploadDir.getPath());
        PairList paramMap = null;
        if (ServletFileUpload.isMultipartContent(request)) {
            paramMap = getMultipartParameters(request, uploadHandler, LOG, tempUploadDir.getPath());
            if (paramMap.size() == 0) {
                LOG.debug("UploadServlet Parsed multipart request and " + "found no parameters. Parsing as regular" + " request instead.");
                paramMap = copyParameters(request);
                LOG.debug("UploadServlet Parsed as regular request and found " + paramMap.size() + " parameters.");
            }
        } else {
            paramMap = copyParameters(request);
        }
        String communityId = getCurrentCommunityId(request.getSession());
        String newcommunity = paramMap.getValue(HttpParams.UP2P_COMMUNITY);
        LOG.debug("UploadServlet: Got active community: " + newcommunity);
        if (newcommunity != null) {
            communityId = newcommunity;
            LOG.debug("switching to community" + communityId);
        }
        if (communityId == null || communityId.length() == 0) {
            LOG.warn("UploadServlet Current community ID is missing from" + "the user session.");
            writeError(request, response, "The current community is unknown." + " Please select a community before performing " + "any actions.", paramMap);
            for (File f : tempUploadDir.listFiles()) {
                f.delete();
            }
            tempUploadDir.delete();
            return;
        }
        LOG.info("UploadServlet Uploading to community " + communityId + ".");
        uploadFiles.clear();
        Iterator<String> uploadedFileIter = paramMap.getValues(HttpParams.UP2P_FILENAME);
        String filename = "";
        try {
            if (uploadedFileIter.hasNext()) {
                filename = uploadedFileIter.next();
            } else {
                throw new IOException("UploadServlet: No up2p:filename parameters were found.");
            }
            if (filename.startsWith("file:")) filename = filename.substring(5);
            if (filename.length() == 0) {
                throw new IOException("UploadServlet: An empty up2p:filename parameter was submitted.");
            }
            File resourceFile = null;
            resourceFile = new File(adapter.getStorageDirectory(communityId), filename);
            File tempResFile = new File(tempUploadDir, filename);
            if (!tempResFile.exists() && !resourceFile.exists()) {
                throw new IOException("UploadServlet: The uploaded resource could not be found.");
            }
            if ((tempResFile.exists() && resourceFile.exists()) || (tempResFile.exists() && !resourceFile.exists())) {
                resourceFile = FileUtil.createUniqueFile(resourceFile);
                LOG.info("UploadServlet: Direct upload, copying resource file.\n\tOriginal: " + tempResFile.getPath() + "\n\tNew: " + resourceFile.getPath());
                resourceFile.getParentFile().mkdir();
                FileOutputStream resourceCopyStream = new FileOutputStream(resourceFile);
                FileUtil.writeFileToStream(resourceCopyStream, tempResFile, true);
                filename = resourceFile.getName();
            }
            LOG.info("UploadServlet: resource file name: " + filename);
            uploadFiles.add(resourceFile);
        } catch (IOException e) {
            LOG.error("UploadServlet: " + e.getMessage());
            writeError(request, response, e.getMessage(), paramMap);
            return;
        }
        String editResourceId = paramMap.getValue(HttpParams.UP2P_EDIT_RESOURCE);
        if (editResourceId != null && editResourceId.length() > 0) {
            LOG.debug("UploadServlet: Got edit resource: " + editResourceId);
            File editAttach = new File(adapter.getAttachmentStorageDirectory(communityId, editResourceId));
            if (editAttach.exists() && editAttach.isDirectory()) {
                LOG.debug("UploadServlet: Copying additional attachments from: " + editAttach.getAbsolutePath());
                for (File oldFile : editAttach.listFiles()) {
                    File newFile = new File(tempUploadDir, oldFile.getName());
                    if (!newFile.exists()) {
                        LOG.debug("UploadServlet: Copying attachment: " + oldFile.getAbsolutePath() + " to " + newFile.getAbsolutePath());
                        FileOutputStream attachCopyStream = new FileOutputStream(newFile);
                        FileUtil.writeFileToStream(attachCopyStream, oldFile, true);
                    } else {
                        LOG.debug("UploadServlet: Attachment " + newFile.getName() + " explicitly replaced in new upload.");
                    }
                }
            }
        }
        String batchUploadString = paramMap.getValue(HttpParams.UP2P_BATCH);
        boolean batchUpload = batchUploadString != null && batchUploadString.length() > 0;
        if (batchUpload) {
            LOG.info("UploadSerlvet Recieved batch upload request.");
            File batchFile = new File(adapter.getStorageDirectory(communityId), uploadFiles.get(0).getName());
            uploadFiles.clear();
            LOG.debug("UploadServlet resource file stored at: " + batchFile.getPath());
            XMLReader reader = TransformerHelper.getXMLReader();
            reader.setContentHandler(new BatchCopyHandler(communityId, uploadFiles));
            try {
                FileInputStream batchInput = new FileInputStream(batchFile);
                reader.parse(new InputSource(batchInput));
                batchInput.close();
            } catch (Exception e) {
                LOG.error("UploadServlet: Error parsing batch upload file.");
                LOG.error("UploadServlet: " + e.getMessage());
                writeError(request, response, "Uploaded content was not a valid batch resource file.", paramMap);
                return;
            }
            batchFile.delete();
            adapter.addNotification("Batch file succesfully processed into " + uploadFiles.size() + " resources.");
        }
        boolean pushUpload = paramMap.getValue(HttpParams.UP2P_PUSH) != null && paramMap.getValue(HttpParams.UP2P_PUSH).length() > 0;
        String id = "";
        boolean duplicateResource = false;
        int uploadCount = 0;
        for (int i = 0; i < uploadFiles.size(); i++) {
            LOG.info("UploadServlet Publishing resource to WebAdapter: " + uploadFiles.get(i).getName());
            try {
                id = adapter.publish(communityId, new File(uploadFiles.get(i).getName()), tempUploadDir);
                uploadFiles.remove(i);
                i--;
                uploadCount++;
                LOG.info("UploadServlet Resource published with id: " + id);
            } catch (IOException e) {
                LOG.warn("UploadServlet IO Error occured in reading the uploaded file: " + e.getMessage(), e);
                uploadFilesCleanup(uploadFiles, tempUploadDir);
                writeError(request, response, "An error occured in reading the uploaded file: " + e.getMessage(), paramMap);
                return;
            } catch (SAXParseException e) {
                LOG.warn("UploadServlet SAX Parse Error occured in uploaded resource: " + e.getMessage());
                String errMsg = "Invalid XML in the uploaded resource.<br/>" + e.getMessage() + "<br/><br/>File location: " + uploadFiles.get(i).getAbsolutePath() + "<br/>Line: " + e.getLineNumber() + " Column: " + e.getColumnNumber();
                uploadFilesCleanup(uploadFiles, tempUploadDir);
                writeError(request, response, errMsg, paramMap);
                return;
            } catch (SAXException e) {
                LOG.warn("UploadServlet Invalid XML in uploaded resource: " + e.getMessage());
                String errMsg = "Invalid XML in uploaded resource<br/><i>" + e.getMessage() + "</i><br/>" + "File location: " + uploadFiles.get(i).getAbsolutePath();
                uploadFilesCleanup(uploadFiles, tempUploadDir);
                writeError(request, response, errMsg, paramMap);
                return;
            } catch (DuplicateResourceException e) {
                LOG.info("UploadServlet Duplicate Resource: " + e.getResourceId() + " Community: " + e.getCommunityId());
                if (batchUpload) {
                    if (!duplicateResource) {
                        adapter.addNotification("Warning: Batch upload contained previously published " + " resources which have been discarded.");
                        duplicateResource = true;
                    }
                } else {
                    uploadFilesCleanup(uploadFiles, tempUploadDir);
                    String respondWithXml = paramMap.getValue(HttpParams.UP2P_FETCH_XML);
                    if (respondWithXml != null && respondWithXml.length() > 0) {
                        writeError(request, response, "This resource is already shared with resource " + " id: " + e.getResourceId(), paramMap);
                        return;
                    }
                    String redirect = response.encodeURL("/overwrite.jsp?up2p:community=" + e.getCommunityId() + "&up2p:resource=" + e.getResourceId());
                    LOG.info("UploadServlet Redirecting to " + redirect);
                    RequestDispatcher rd = request.getRequestDispatcher(redirect);
                    rd.forward(request, response);
                    return;
                }
            } catch (NetworkAdapterException e) {
                LOG.info("UploadServlet Error in the Network Adapter for" + " community ID " + communityId, e);
                uploadFilesCleanup(uploadFiles, tempUploadDir);
                writeError(request, response, "Error in the Network Adapter for this community. <br/>" + e.getMessage(), paramMap);
                return;
            } catch (ResourceNotFoundException e) {
                LOG.info("UploadServlet Error Resource not found " + e);
                uploadFilesCleanup(uploadFiles, tempUploadDir);
                writeError(request, response, "Error : <br/>" + e.getMessage(), paramMap);
                return;
            }
        }
        uploadFilesCleanup(uploadFiles, tempUploadDir);
        String ajaxRequest = paramMap.getValue(HttpParams.UP2P_XMLHTTP);
        String respondWithXml = paramMap.getValue(HttpParams.UP2P_FETCH_XML);
        if (respondWithXml != null && respondWithXml.length() > 0) {
            response.setContentType("text/xml");
            PrintWriter out = response.getWriter();
            out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            out.println("<upload success=\"true\" >");
            out.println("<resid>" + id + "</resid>");
            out.println("</upload>");
        } else if (ajaxRequest != null && ajaxRequest.length() > 0) {
            LOG.debug("UploadServlet Recieved xmlHttp request, responding with XML");
            response.setContentType("text/xml");
            PrintWriter out = response.getWriter();
            out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            out.print("<resource id=\"" + request.getParameter(HttpParams.UP2P_RESOURCE) + "\" ");
            if (request.getParameter(HttpParams.UP2P_PEERID) != null) {
                out.print("peerid=\"" + request.getParameter(HttpParams.UP2P_PEERID) + "\" ");
            }
            out.println("/>");
        } else if (batchUpload) {
            adapter.addNotification(uploadCount + " resources were succesfully published.");
            request.setAttribute("up2p.display.mode", "view");
            String redirect = response.encodeURL("/view.jsp?up2p:community=" + communityId);
            LOG.info("UploadServlet Redirecting to " + redirect);
            RequestDispatcher rd = request.getRequestDispatcher(redirect);
            rd.forward(request, response);
        } else if (!pushUpload) {
            request.setAttribute("up2p.display.mode", "view");
            String redirect = response.encodeURL("/view.jsp?up2p:resource=" + id);
            LOG.info("UploadServlet Redirecting to " + redirect);
            RequestDispatcher rd = request.getRequestDispatcher(redirect);
            rd.forward(request, response);
        }
        return;
    }
