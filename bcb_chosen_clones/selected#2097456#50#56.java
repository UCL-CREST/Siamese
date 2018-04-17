    protected void displayHouse(int rowNum) throws URISyntaxException, IOException {
        if (!Desktop.isDesktopSupported()) {
            format("'map' is not supported on this platofrm.\n");
            return;
        }
        Desktop.getDesktop().browse(new URI(String.format(googleFmt, getAddress(rowNum).replaceAll(" ", "+"))));
    }
