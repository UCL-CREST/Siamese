    public void createResource(String resourceUri, boolean publish, User user) throws IOException {
        PermissionAPI perAPI = APILocator.getPermissionAPI();
        Logger.debug(this.getClass(), "createResource");
        resourceUri = stripMapping(resourceUri);
        String hostName = getHostname(resourceUri);
        String path = getPath(resourceUri);
        String folderName = getFolderName(path);
        String fileName = getFileName(path);
        fileName = deleteSpecialCharacter(fileName);
        if (fileName.startsWith(".")) {
            return;
        }
        Host host = HostFactory.getHostByHostName(hostName);
        Folder folder = FolderFactory.getFolderByPath(folderName, host);
        boolean hasPermission = perAPI.doesUserHavePermission(folder, PERMISSION_WRITE, user, false);
        if (hasPermission) {
            if (!checkFolderFilter(folder, fileName)) {
                throw new IOException("The file doesn't comply the folder's filter");
            }
            if (host.getInode() != 0 && folder.getInode() != 0) {
                File file = new File();
                file.setTitle(fileName);
                file.setFileName(fileName);
                file.setShowOnMenu(false);
                file.setLive(publish);
                file.setWorking(true);
                file.setDeleted(false);
                file.setLocked(false);
                file.setModDate(new Date());
                String mimeType = FileFactory.getMimeType(fileName);
                file.setMimeType(mimeType);
                String author = user.getFullName();
                file.setAuthor(author);
                file.setModUser(author);
                file.setSortOrder(0);
                file.setShowOnMenu(false);
                try {
                    Identifier identifier = null;
                    if (!isResource(resourceUri)) {
                        WebAssetFactory.createAsset(file, user.getUserId(), folder, publish);
                        identifier = IdentifierCache.getIdentifierFromIdentifierCache(file);
                    } else {
                        File actualFile = FileFactory.getFileByURI(path, host, false);
                        identifier = IdentifierCache.getIdentifierFromIdentifierCache(actualFile);
                        WebAssetFactory.createAsset(file, user.getUserId(), folder, identifier, false, false);
                        WebAssetFactory.publishAsset(file);
                        String assetsPath = FileFactory.getRealAssetsRootPath();
                        new java.io.File(assetsPath).mkdir();
                        java.io.File workingIOFile = FileFactory.getAssetIOFile(file);
                        DotResourceCache vc = CacheLocator.getVeloctyResourceCache();
                        vc.remove(ResourceManager.RESOURCE_TEMPLATE + workingIOFile.getPath());
                        if (file != null && file.getInode() > 0) {
                            byte[] currentData = new byte[0];
                            FileInputStream is = new FileInputStream(workingIOFile);
                            int size = is.available();
                            currentData = new byte[size];
                            is.read(currentData);
                            java.io.File newVersionFile = FileFactory.getAssetIOFile(file);
                            vc.remove(ResourceManager.RESOURCE_TEMPLATE + newVersionFile.getPath());
                            FileChannel channelTo = new FileOutputStream(newVersionFile).getChannel();
                            ByteBuffer currentDataBuffer = ByteBuffer.allocate(currentData.length);
                            currentDataBuffer.put(currentData);
                            currentDataBuffer.position(0);
                            channelTo.write(currentDataBuffer);
                            channelTo.force(false);
                            channelTo.close();
                        }
                        java.util.List<Tree> parentTrees = TreeFactory.getTreesByChild(file);
                        for (Tree tree : parentTrees) {
                            Tree newTree = TreeFactory.getTree(tree.getParent(), file.getInode());
                            if (newTree.getChild() == 0) {
                                newTree.setParent(tree.getParent());
                                newTree.setChild(file.getInode());
                                newTree.setRelationType(tree.getRelationType());
                                newTree.setTreeOrder(0);
                                TreeFactory.saveTree(newTree);
                            }
                        }
                    }
                    List<Permission> permissions = perAPI.getPermissions(folder);
                    for (Permission permission : permissions) {
                        Permission filePermission = new Permission();
                        filePermission.setPermission(permission.getPermission());
                        filePermission.setRoleId(permission.getRoleId());
                        filePermission.setInode(identifier.getInode());
                        perAPI.save(filePermission);
                    }
                } catch (Exception ex) {
                    Logger.debug(this, ex.toString());
                }
            }
        } else {
            throw new IOException("You don't have access to add that folder/host");
        }
    }
