    public void addResourcesFromFolder(final ZipOutStreamTaskAdapter out, final TreeItemDecorator<FolderEntity> folder, final String zipPath) throws IOException, TaskTimeoutException {
        addFolder(out, folder.getEntity(), zipPath);
        List<String> childrenNames = new ArrayList<String>();
        for (TreeItemDecorator<FolderEntity> child : folder.getChildren()) {
            addResourcesFromFolder(out, child, zipPath + child.getEntity().getName() + "/");
            childrenNames.add(child.getEntity().getName());
        }
        if (zipPath.startsWith("page/")) {
            String pageURL = zipPath.replace("page", "");
            if (!pageURL.equals("/")) {
                pageURL = pageURL.substring(0, pageURL.length() - 1);
            }
            List<PageEntity> children = getDao().getPageDao().getByParent(pageURL);
            for (PageEntity child : children) {
                if (!childrenNames.contains(child.getPageFriendlyURL())) {
                    addResourcesFromPage(out, child.getFriendlyURL(), zipPath + child.getPageFriendlyURL() + "/");
                }
            }
            List<PageEntity> pages = getDao().getPageDao().selectByUrl(pageURL);
            if (pages.size() > 0) {
                addPageFiles(out, pages.get(0), zipPath);
            }
        }
        List<FileEntity> files = getDao().getFileDao().getByFolder(folder.getEntity().getId());
        for (FileEntity file : files) {
            String filePath = zipPath + file.getFilename();
            if (!out.isSkip(filePath)) {
                out.putNextEntry(new ZipEntry(filePath));
                out.write(getDao().getFileDao().getFileContent(file));
                out.closeEntry();
                out.nextFile();
            }
        }
    }
