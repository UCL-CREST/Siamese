    public void hyperlinkUpdate(HyperlinkEvent event) {
        URL url = event.getURL();
        if (event.getEventType() == HyperlinkEvent.EventType.ENTERED && !event.getDescription().equals("login") && !event.getDescription().startsWith("copyUrl:")) {
            this.resultsText.setToolTipText(url.toString());
        } else if (event.getEventType() == HyperlinkEvent.EventType.EXITED) {
            this.resultsText.setToolTipText(null);
        } else if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            if (event.getDescription().equals("login")) {
                if (this.win == null) {
                    this.win = new WinLoginWindow(this.extension, this, this.keys, this.reposPath);
                } else {
                    this.win.setVisible(true);
                }
            } else if (event.getDescription().startsWith("copyUrl:")) {
                String toCopy = event.getDescription().replaceFirst("copyUrl:", "");
                StringSelection data = new StringSelection(toCopy);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(data, data);
            } else {
                if (!Desktop.isDesktopSupported()) {
                    try {
                        Runtime.getRuntime().exec("firefox " + url.toString());
                    } catch (Exception ex) {
                        return;
                    }
                }
                try {
                    Desktop desktop = Desktop.getDesktop();
                    if (!desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
                        return;
                    }
                    URI uri = new URI(url.toString());
                    desktop.browse(uri);
                } catch (Exception e) {
                }
            }
        }
    }
