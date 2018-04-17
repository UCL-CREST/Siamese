    public void hyperlinkUpdate(HyperlinkEvent event) {
        URL url = event.getURL();
        if (event.getEventType() == HyperlinkEvent.EventType.ENTERED && !event.getDescription().startsWith("copyUrl:")) {
            updatesEditorPane.setToolTipText(url.toString());
        } else if (event.getEventType() == HyperlinkEvent.EventType.EXITED) {
            updatesEditorPane.setToolTipText(null);
        } else if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            if (event.getDescription().startsWith("copyUrl:")) {
                String toCopy = event.getDescription().replaceFirst("copyUrl:", "");
                StringSelection data = new StringSelection(toCopy);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(data, data);
            } else {
                String browser = configManager.getBrowser();
                if (!Desktop.isDesktopSupported()) {
                    try {
                        if (!browser.equals("")) {
                            Runtime.getRuntime().exec(browser + " " + url.toString());
                        } else {
                            Runtime.getRuntime().exec("firefox " + url.toString());
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Unable to find a web browser, please set up one on settings window", "Web browser error", JOptionPane.WARNING_MESSAGE);
                    }
                }
                try {
                    Desktop desktop = Desktop.getDesktop();
                    URI uri = new URI(url.toString());
                    desktop.browse(uri);
                } catch (Exception e) {
                    return;
                }
            }
        }
    }
