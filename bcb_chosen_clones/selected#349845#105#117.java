    private void titelOeffnen() {
        if (!textarray[Konstanten.FILM_URL_THEMA_NR].getText().equals("")) {
            if (Desktop.isDesktopSupported()) {
                Desktop d = Desktop.getDesktop();
                try {
                    if (d.isSupported(Desktop.Action.BROWSE)) {
                        d.browse(new URI(textarray[Konstanten.FILM_URL_THEMA_NR].getText()));
                    }
                } catch (Exception ex) {
                }
            }
        }
    }
