    @Override
    public void widgetSelected(SelectionEvent e) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(e.text));
            } catch (IOException e1) {
                GroofyLogger.getInstance().logException(e1);
            } catch (URISyntaxException e1) {
                GroofyLogger.getInstance().logException(e1);
            }
        }
    }
