    public synchronized void fileUpload(UploadEvent event) throws Exception {
        UploadItem item = event.getUploadItem();
        if (StringUtils.isNotEmpty(item.getFileName())) {
            byte[] byteCode = IOUtils.toByteArray(new FileInputStream(item.getFile()));
            String fileName = new File(StringUtils.replace(item.getFileName(), "\\", "/")).getName();
            log.info("Uploading filecontent: {0}", fileName);
            fileName = fileName.replace(".class", "");
            JavaClass typeClass = new ClassParser(new ByteArrayInputStream(byteCode), fileName).parse();
            String packageName = typeClass.getPackageName();
            Folder packageFolder = null;
            for (Folder rootFolder : roots) {
                if (rootFolder.getName().equals(packageName)) packageFolder = rootFolder;
            }
            if (packageFolder == null) {
                packageFolder = new Folder(packageName);
                packageFolder = folderDao.create(FolderDao.TYPES_ROOT_PATH, packageFolder);
            }
            ContentType existingType = null;
            for (ContentFile contentFile : packageFolder.getFiles()) {
                if (contentFile.getName().equals(fileName)) {
                    existingType = (ContentType) contentFile;
                }
            }
            JcrDataProviderImpl jcrDataProvider = new JcrDataProviderImpl(JcrDataProvider.TYPE.BYTES, byteCode);
            if (existingType != null) {
                existingType.getContent().setDataProvider(jcrDataProvider);
                existingType.getContent().setLastModified(Calendar.getInstance());
                fileDao.update(existingType);
                log.info("Updated content type '#0' with new bytecode", typeClass.getClassName());
            } else {
                ContentType contentType = new ContentType(fileName);
                contentType.setClassName(typeClass.getClassName());
                contentType.getContent().setLastModified(Calendar.getInstance());
                contentType.getContent().setMimeType(MimeType.CLASS);
                contentType.getContent().setDataProvider(jcrDataProvider);
                packageFolder.getFiles().add(contentType);
                fileDao.create(contentType, packageFolder);
                cmsController.addType(contentType);
                log.info("Added new type '#0'", typeClass.getClassName());
            }
            this.setSelectedFolder(packageFolder);
        }
    }
