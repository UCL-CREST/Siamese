    public boolean authorizeToken() throws Exception {
        String url;
        if (this.provider.getAuthorizationUrl().indexOf('?') == -1) {
            url = this.provider.getAuthorizationUrl() + "?oauth_token=" + this.token;
        } else {
            url = this.provider.getAuthorizationUrl() + "&oauth_token=" + this.token;
        }
        URI authUrl = new URI(url);
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                desktop.browse(authUrl);
            } else {
                JOptionPane.showMessageDialog(null, "Visit the following URL: " + authUrl);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Visit the following URL: " + authUrl);
        }
        verificationCode = JOptionPane.showInputDialog("Please enter the verification Code");
        return true;
    }
