    public void notify(final UIEvent evt) {
        if (evt.isOfType(IReplayTune.class)) {
            if (getPlayer().getTune() != null) {
                playTune(getPlayer().getTune().getInfo().file);
            } else {
                playTune(null);
            }
        } else if (evt.isOfType(IPlayTune.class)) {
            IPlayTune ifObj = (IPlayTune) evt.getUIEventImpl();
            if (evt.isOfType(Reset.class)) {
                getPlayer().setCommand(((Reset) evt.getUIEventImpl()).getCommand());
            }
            playTune(ifObj.getFile());
        } else if (evt.isOfType(IGotoURL.class)) {
            IGotoURL ifObj = (IGotoURL) evt.getUIEventImpl();
            if (isActive()) {
                getAppletContext().showDocument(ifObj.getCollectionURL(), "_blank");
            } else {
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    if (desktop.isSupported(Desktop.Action.BROWSE)) {
                        try {
                            desktop.browse(ifObj.getCollectionURL().toURI());
                        } catch (final IOException ioe) {
                            ioe.printStackTrace();
                        } catch (final URISyntaxException urie) {
                            urie.printStackTrace();
                        }
                    }
                }
            }
        } else if (evt.isOfType(IStopTune.class)) {
            stopC64();
        } else if (evt.isOfType(IInsertMedia.class)) {
            IInsertMedia ifObj = (IInsertMedia) evt.getUIEventImpl();
            File mediaFile = ifObj.getSelectedMedia();
            try {
                if (mediaFile instanceof ZipEntryFileProxy) {
                    mediaFile = ZipEntryFileProxy.extractFromZip((ZipEntryFileProxy) mediaFile);
                }
                if (mediaFile.getName().endsWith(".gz")) {
                    mediaFile = ZipEntryFileProxy.extractFromGZ(mediaFile);
                }
                switch(ifObj.getMediaType()) {
                    case TAPE:
                        insertTape(mediaFile, ifObj.getAutostartFile(), ifObj.getComponent());
                        break;
                    case DISK:
                        insertDisk(mediaFile, ifObj.getAutostartFile(), ifObj.getComponent());
                        break;
                    case CART:
                        insertCartridge(mediaFile);
                        break;
                    default:
                        break;
                }
            } catch (IOException e) {
                System.err.println(String.format("Cannot attach file '%s'.", mediaFile.getAbsolutePath()));
                return;
            }
        }
    }
