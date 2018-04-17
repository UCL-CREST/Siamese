    public void doPerform() {
        if (!java.awt.Desktop.isDesktopSupported()) {
            log.error("Desktop is not supported (fatal)");
        } else {
            final java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
            Action actionType = Action.BROWSE;
            if (file != null) {
                actionType = Action.OPEN;
            }
            if (!desktop.isSupported(actionType)) {
                log.error("Desktop doesn't support the " + actionType.name() + " action (fatal)");
            } else {
                try {
                    if (uri != null) {
                        desktop.browse(uri);
                    } else if (file != null) {
                        desktop.open(file);
                    }
                } catch (Exception e1) {
                    log.error(e1.getMessage());
                }
            }
        }
    }
