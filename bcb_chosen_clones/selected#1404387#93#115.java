    public static void openURL(String address) {
        if (address != null && address.startsWith("room://")) {
            int idx = address.lastIndexOf("/");
            Protocol.joinRoomByID(address.substring(idx + 1), "");
            return;
        }
        try {
            Desktop desktop = null;
            if (Desktop.isDesktopSupported()) {
                desktop = Desktop.getDesktop();
            }
            if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                URI uri = null;
                uri = new URI(address);
                desktop.browse(uri);
            }
        } catch (URISyntaxException use) {
        } catch (FileNotFoundException fne) {
        } catch (java.io.IOException ioe) {
        } catch (Exception e) {
            ErrorHandler.handle(e);
        }
    }
