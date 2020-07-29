    public static void openDonationLink(String uri) throws IOException {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            URI donationURI;
            try {
                donationURI = new URI(uri);
                desktop.browse(donationURI);
            } catch (URISyntaxException e) {
            }
        }
    }
