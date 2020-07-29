    public void createScreenCapture(String outputFileName) {
        try {
            Point pt = new Point();
            int width = 0;
            int height = 0;
            if (movieMode == 0) {
                pt = threeDpanel.getWwdLocationOnScreen();
                width = threeDpanel.getWwdWidth();
                height = threeDpanel.getWwdHeight();
            } else if (movieMode == 1) {
                int[] twoDinfo = calculate2DMapSizeAndScreenLoc(twoDpanel);
                pt.setLocation(twoDinfo[2], twoDinfo[3]);
                width = twoDinfo[0];
                height = twoDinfo[1];
            } else {
                pt = otherPanel.getLocationOnScreen();
                width = otherPanel.getWidth();
                height = otherPanel.getHeight();
            }
            if (height <= 0 || width <= 0) {
                JOptionPane.showInternalMessageDialog(this, "A Screenshot was not possible - too small of size", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            BufferedImage screencapture = new Robot().createScreenCapture(new Rectangle(pt.x, pt.y, width, height));
            String fileExtension = "jpg";
            File file = new File(outputFileName);
            Exception e = SaveImageFile.saveImage(fileExtension, file, screencapture, 0.9f);
            if (e != null) {
                System.out.println("ERROR SCREEN CAPTURE:" + e.toString());
                return;
            }
        } catch (Exception e4) {
            System.out.println("ERROR SCREEN CAPTURE:" + e4.toString());
        }
    }
