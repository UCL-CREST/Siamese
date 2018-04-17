    protected File takeSnapShot(Rectangle extent) {
        File snapShotFile = null;
        try {
            snapShotFile = File.createTempFile("snap", ".jpg", new File(System.getProperty("user.home")));
            java.awt.image.BufferedImage snapShotImage = new Robot().createScreenCapture(extent);
            com.sun.image.codec.jpeg.JPEGCodec.createJPEGEncoder(new FileOutputStream(snapShotFile)).encode(snapShotImage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return snapShotFile;
    }
