    public void _saveWebAsset(ActionRequest req, ActionResponse res, PortletConfig config, ActionForm form, User user, String subcmd) throws WebAssetException, Exception {
        long maxsize = 50;
        long maxwidth = 3000;
        long maxheight = 3000;
        long minheight = 10;
        ActionRequestImpl reqImpl = (ActionRequestImpl) req;
        HttpServletRequest httpReq = reqImpl.getHttpServletRequest();
        try {
            UploadPortletRequest uploadReq = PortalUtil.getUploadPortletRequest(req);
            String parent = ParamUtil.getString(req, "parent");
            int countFiles = ParamUtil.getInteger(req, "countFiles");
            int fileCounter = 0;
            Folder folder = (Folder) InodeFactory.getInode(parent, Folder.class);
            _checkUserPermissions(folder, user, PERMISSION_WRITE);
            String userId = user.getUserId();
            String customMessage = "Some file does not match the filters specified by the folder: ";
            boolean filterError = false;
            for (int k = 0; k < countFiles; k++) {
                File file = new File();
                String title = ParamUtil.getString(req, "title" + k);
                String friendlyName = ParamUtil.getString(req, "friendlyName" + k);
                Date publishDate = new Date();
                String fileName = ParamUtil.getString(req, "fileName" + k);
                fileName = checkMACFileName(fileName);
                if (!FolderFactory.matchFilter(folder, fileName)) {
                    customMessage += fileName + ", ";
                    filterError = true;
                    continue;
                }
                if (fileName.length() > 0) {
                    String mimeType = FileFactory.getMimeType(fileName);
                    String URI = folder.getPath() + fileName;
                    String suffix = UtilMethods.getFileExtension(fileName);
                    file.setTitle(title);
                    file.setFileName(fileName);
                    file.setFriendlyName(friendlyName);
                    file.setPublishDate(publishDate);
                    file.setModUser(userId);
                    InodeFactory.saveInode(file);
                    String filePath = FileFactory.getRealAssetsRootPath();
                    new java.io.File(filePath).mkdir();
                    java.io.File uploadedFile = uploadReq.getFile("uploadedFile" + k);
                    Logger.debug(this, "bytes" + uploadedFile.length());
                    file.setSize((int) uploadedFile.length() - 2);
                    file.setMimeType(mimeType);
                    Host host = HostFactory.getCurrentHost(httpReq);
                    Identifier ident = IdentifierFactory.getIdentifierByURI(URI, host);
                    String message = "";
                    if ((FileFactory.existsFileName(folder, fileName))) {
                        InodeFactory.deleteInode(file);
                        message = "The uploaded file " + fileName + " already exists in this folder";
                        SessionMessages.add(req, "custommessage", message);
                    } else {
                        String fileInodePath = String.valueOf(file.getInode());
                        if (fileInodePath.length() == 1) {
                            fileInodePath = fileInodePath + "0";
                        }
                        fileInodePath = fileInodePath.substring(0, 1) + java.io.File.separator + fileInodePath.substring(1, 2);
                        new java.io.File(filePath + java.io.File.separator + fileInodePath.substring(0, 1)).mkdir();
                        new java.io.File(filePath + java.io.File.separator + fileInodePath).mkdir();
                        java.io.File f = new java.io.File(filePath + java.io.File.separator + fileInodePath + java.io.File.separator + file.getInode() + "." + suffix);
                        java.io.FileOutputStream fout = new java.io.FileOutputStream(f);
                        FileChannel outputChannel = fout.getChannel();
                        FileChannel inputChannel = new java.io.FileInputStream(uploadedFile).getChannel();
                        outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
                        outputChannel.force(false);
                        outputChannel.close();
                        inputChannel.close();
                        Logger.debug(this, "SaveFileAction New File in =" + filePath + java.io.File.separator + fileInodePath + java.io.File.separator + file.getInode() + "." + suffix);
                        if (suffix.equals("jpg") || suffix.equals("gif")) {
                            com.dotmarketing.util.Thumbnail.resizeImage(filePath + java.io.File.separator + fileInodePath + java.io.File.separator, String.valueOf(file.getInode()), suffix);
                            int height = javax.imageio.ImageIO.read(f).getHeight();
                            file.setHeight(height);
                            Logger.debug(this, "File height=" + height);
                            int width = javax.imageio.ImageIO.read(f).getWidth();
                            file.setWidth(width);
                            Logger.debug(this, "File width=" + width);
                            long size = (f.length() / 1024);
                            WebAssetFactory.createAsset(file, userId, folder);
                        } else {
                            WebAssetFactory.createAsset(file, userId, folder);
                        }
                        WorkingCache.addToWorkingAssetToCache(file);
                        _setFilePermissions(folder, file, user);
                        fileCounter += 1;
                        if ((subcmd != null) && subcmd.equals(com.dotmarketing.util.Constants.PUBLISH)) {
                            try {
                                PublishFactory.publishAsset(file, httpReq);
                                if (fileCounter > 1) {
                                    SessionMessages.add(req, "message", "message.file_asset.save");
                                } else {
                                    SessionMessages.add(req, "message", "message.fileupload.save");
                                }
                            } catch (WebAssetException wax) {
                                Logger.error(this, wax.getMessage(), wax);
                                SessionMessages.add(req, "error", "message.webasset.published.failed");
                            }
                        }
                    }
                }
            }
            if (filterError) {
                customMessage = customMessage.substring(0, customMessage.lastIndexOf(","));
                SessionMessages.add(req, "custommessage", customMessage);
            }
        } catch (IOException e) {
            Logger.error(this, "Exception saving file: " + e.getMessage());
            throw new ActionException(e.getMessage());
        }
    }
