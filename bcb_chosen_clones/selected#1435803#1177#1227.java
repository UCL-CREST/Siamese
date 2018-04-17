    private void captureScreen(String invoc) {
        String parm;
        String oldImageFilename;
        String newImageFilename;
        StringTokenizer st;
        int x;
        int y;
        int w;
        int h;
        BufferedImage newBufferedImage;
        Rectangle rect;
        imageCounter++;
        st = new StringTokenizer(invoc);
        if (st.countTokens() < 5) {
            logPrint("Invalid Screen Capture command " + invoc);
            errcode = SCREENCAPTURE;
            return;
        }
        try {
            parm = st.nextToken();
            x = Integer.parseInt(parm);
            parm = st.nextToken();
            y = Integer.parseInt(parm);
            parm = st.nextToken();
            w = Integer.parseInt(parm);
            parm = st.nextToken();
            h = Integer.parseInt(parm);
            oldImageFilename = st.nextToken();
        } catch (Exception e) {
            logPrint("Invalid Screen Capture command " + invoc);
            errcode = SCREENCAPTURE;
            return;
        }
        try {
            robbie.delay(3000);
            rect = new Rectangle(x, y, w, h);
            newBufferedImage = robbie.createScreenCapture(rect);
            newImageFilename = new String("Testimage" + imageCounter + ".JPG");
            writeJPEG(newBufferedImage, newImageFilename);
            logPrint("Test Screen image saved as " + newImageFilename);
            parm = oldImageFilename + " " + newImageFilename;
            fileCompare(parm);
        } catch (Exception e) {
            String s = new String("System cannot compare image; exception " + e);
            System.out.println(s);
            if (verbose) {
                JOptionPane.showMessageDialog(null, s, "SevereError", JOptionPane.WARNING_MESSAGE);
            }
            errcode = SCREENCAPTURE;
        }
    }
