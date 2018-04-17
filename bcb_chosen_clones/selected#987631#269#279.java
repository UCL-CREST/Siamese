    public void webpageAction() {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(controller.getUrlLinks().getWebpageUri());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(getFrame(), "Can't find the default web browser\nto open the web page " + controller.getUrlLinks().getWebpageUrlStr() + ".", "Cannot open web page", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(getFrame(), "Operation not supported on this platform.\nPlease go to " + controller.getUrlLinks().getWebpageUrlStr(), "Cannot open web page", JOptionPane.INFORMATION_MESSAGE);
        }
    }
