    public void actionPerformed(ActionEvent e) {
        if (path.compareTo("") != 0) {
            imageName = (path.substring(path.lastIndexOf(imageFile.separator) + 1, path.length()));
            File imageFile = new File(path);
            directoryPath = "Images" + imageFile.separator + imageName.substring(0, 1).toUpperCase();
            File directory = new File(directoryPath);
            directory.mkdirs();
            imagePath = "." + imageFile.separator + "Images" + imageFile.separator + imageName.substring(0, 1).toUpperCase() + imageFile.separator + imageName;
            File newFile = new File(imagePath);
            int i = 1;
            while (newFile.exists()) {
                imagePath = "." + imageFile.separator + "Images" + imageFile.separator + imageName.substring(0, imageName.lastIndexOf('.')) + "_" + i + imageName.substring(imageName.lastIndexOf('.'), imageName.length());
                newFile = new File(imagePath);
                i++;
            }
            imagePathThumb = (imagePath.substring(0, imagePath.lastIndexOf("."))).concat("_th.jpg");
            dataBase.insertDB(theConcepts, imageName, imageName.substring(0, imageName.lastIndexOf('.')));
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
                    try {
                        int thumbWidth = PREVIEW_WIDTH;
                        int thumbHeight = PREVIEW_HEIGHT;
                        double thumbRatio = (double) thumbWidth / (double) thumbHeight;
                        int imageWidth = image.getIconWidth();
                        int imageHeight = image.getIconHeight();
                        double imageRatio = (double) imageWidth / (double) imageHeight;
                        if (thumbRatio < imageRatio) {
                            thumbHeight = (int) (thumbWidth / imageRatio);
                        } else {
                            thumbWidth = (int) (thumbHeight * imageRatio);
                        }
                        BufferedImage thumbImage = new BufferedImage(thumbWidth, thumbHeight, BufferedImage.TYPE_INT_RGB);
                        Graphics2D graphics2D = thumbImage.createGraphics();
                        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                        graphics2D.drawImage(image.getImage(), 0, 0, thumbWidth, thumbHeight, null);
                        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(imagePathThumb));
                        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
                        JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(thumbImage);
                        int quality = 100;
                        quality = Math.max(0, Math.min(quality, 100));
                        param.setQuality((float) quality / 100.0f, false);
                        encoder.setJPEGEncodeParam(param);
                        encoder.encode(thumbImage);
                        out.close();
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                        System.out.println(ex.toString());
                    }
                }
            }
        }
    }
