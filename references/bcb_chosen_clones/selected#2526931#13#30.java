    @Override
    public void create() {
        if (Desktop.isDesktopSupported()) {
            URI uri;
            try {
                uri = new URI("http://www.myguide.gov.uk/");
                Desktop.getDesktop().browse(uri);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        TabManager.showTab("Users");
        new NewUserDialog();
    }
