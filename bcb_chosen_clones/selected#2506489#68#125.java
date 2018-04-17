    public void actionPerformed(ActionEvent e) {
        if (path.compareTo("") != 0) {
            imageName = (path.substring(path.lastIndexOf(File.separator) + 1, path.length()));
            String name = imageName.substring(0, imageName.lastIndexOf('.'));
            String extension = imageName.substring(imageName.lastIndexOf('.') + 1, imageName.length());
            File imageFile = new File(path);
            directoryPath = "images" + File.separator + imageName.substring(0, 1).toUpperCase();
            File directory = new File(directoryPath);
            directory.mkdirs();
            imagePath = "." + File.separator + "images" + File.separator + imageName.substring(0, 1).toUpperCase() + File.separator + imageName;
            File newFile = new File(imagePath);
            if (myImagesBehaviour.equals(TLanguage.getString("TIGManageGalleryDialog.REPLACE_IMAGE"))) {
                Vector<Vector<String>> aux = TIGDataBase.imageSearchByName(name);
                if (aux.size() != 0) {
                    int idImage = TIGDataBase.imageKeySearchName(name);
                    TIGDataBase.deleteAsociatedOfImage(idImage);
                }
            }
            if (myImagesBehaviour.equals(TLanguage.getString("TIGManageGalleryDialog.ADD_IMAGE"))) {
                int i = 1;
                while (newFile.exists()) {
                    imagePath = "." + File.separator + "images" + File.separator + imageName.substring(0, 1).toUpperCase() + File.separator + imageName.substring(0, imageName.lastIndexOf('.')) + "_" + i + imageName.substring(imageName.lastIndexOf('.'), imageName.length());
                    name = name + "_" + i;
                    newFile = new File(imagePath);
                    i++;
                }
            }
            imagePathThumb = (imagePath.substring(0, imagePath.lastIndexOf("."))).concat("_th.jpg");
            imageName = name + "." + extension;
            try {
                FileChannel srcChannel = new FileInputStream(path).getChannel();
                FileChannel dstChannel = new FileOutputStream(imagePath).getChannel();
                dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
                srcChannel.close();
                dstChannel.close();
            } catch (IOException exc) {
                System.out.println(exc.getMessage());
                System.out.println(exc.toString());
            }
            TIGDataBase.insertDB(theConcepts, imageName, imageName.substring(0, imageName.lastIndexOf('.')));
            image = null;
            if (imageFile != null) {
                if (TFileUtils.isJAIRequired(imageFile)) {
                    RenderedOp src = JAI.create("fileload", imageFile.getAbsolutePath());
                    BufferedImage bufferedImage = src.getAsBufferedImage();
                    image = new ImageIcon(bufferedImage);
                } else {
                    image = new ImageIcon(imageFile.getAbsolutePath());
                }
                if (image.getImageLoadStatus() == MediaTracker.ERRORED) {
                    int choosenOption = JOptionPane.NO_OPTION;
                    choosenOption = JOptionPane.showConfirmDialog(null, TLanguage.getString("TIGInsertImageAction.MESSAGE"), TLanguage.getString("TIGInsertImageAction.NAME"), JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
                } else {
                    createThumbnail();
                }
            }
        }
    }
