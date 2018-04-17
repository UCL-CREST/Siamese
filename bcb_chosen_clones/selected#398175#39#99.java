    protected Object openDialogBox(Control cellEditorWindow) {
        FileDialog dialog = new FileDialog(parent.getShell(), SWT.OPEN);
        dialog.setFilterExtensions(new String[] { "*.jpg;*.JPG;*.JPEG;*.gif;*.GIF;*.png;*.PNG", "*.jpg;*.JPG;*.JPEG", "*.gif;*.GIF", "*.png;*.PNG" });
        dialog.setFilterNames(new String[] { "All", "Joint Photographic Experts Group (JPEG)", "Graphics Interchange Format (GIF)", "Portable Network Graphics (PNG)" });
        String imagePath = dialog.open();
        if (imagePath == null) return null;
        IProject project = ProjectManager.getInstance().getCurrentProject();
        String projectFolderPath = project.getLocation().toOSString();
        File imageFile = new File(imagePath);
        String fileName = imageFile.getName();
        ImageData imageData = null;
        try {
            imageData = new ImageData(imagePath);
        } catch (SWTException e) {
            UserErrorException error = new UserErrorException(PropertyHandler.getInstance().getProperty("_invalid_image_title"), PropertyHandler.getInstance().getProperty("_invalid_image_text"));
            UserErrorService.INSTANCE.showError(error);
            return null;
        }
        if (imageData == null) {
            UserErrorException error = new UserErrorException(PropertyHandler.getInstance().getProperty("_invalid_image_title"), PropertyHandler.getInstance().getProperty("_invalid_image_text"));
            UserErrorService.INSTANCE.showError(error);
            return null;
        }
        File copiedImageFile = new File(projectFolderPath + File.separator + imageFolderPath + File.separator + fileName);
        if (copiedImageFile.exists()) {
            Path path = new Path(copiedImageFile.getPath());
            copiedImageFile = new File(projectFolderPath + File.separator + imageFolderPath + File.separator + UUID.randomUUID().toString() + "." + path.getFileExtension());
        }
        try {
            copiedImageFile.createNewFile();
        } catch (IOException e1) {
            ExceptionHandlingService.INSTANCE.handleException(e1);
            copiedImageFile = null;
        }
        if (copiedImageFile == null) {
            copiedImageFile = new File(projectFolderPath + File.separator + imageFolderPath + File.separator + UUID.randomUUID().toString());
            try {
                copiedImageFile.createNewFile();
            } catch (IOException e) {
                ExceptionHandlingService.INSTANCE.handleException(e);
                return "";
            }
        }
        FileReader in = null;
        FileWriter out = null;
        try {
            in = new FileReader(imageFile);
            out = new FileWriter(copiedImageFile);
            int c;
            while ((c = in.read()) != -1) out.write(c);
            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            ExceptionHandlingService.INSTANCE.handleException(e);
            return "";
        } catch (IOException e) {
            ExceptionHandlingService.INSTANCE.handleException(e);
            return "";
        }
        return imageFolderPath + File.separator + copiedImageFile.getName();
    }
