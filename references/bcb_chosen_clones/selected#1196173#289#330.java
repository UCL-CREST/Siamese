    public void hyperlinkUpdate(HyperlinkEvent e) {
        Desktop desktop = null;
        if (e.getEventType().equals(EventType.ACTIVATED) && e.getURL() != null) {
            String query = e.getURL().getQuery();
            String key = "", value = "";
            if (query != null) {
                key = query.substring(0, e.getURL().getQuery().indexOf('='));
                value = query.substring(e.getURL().getQuery().indexOf('=') + 1);
                log.debug(key);
                log.debug(value);
            }
            if ("view".equals(key)) {
                IVisualizer vis = ExtensionFactory.createVisualizer(value, node);
                if (vis != null) vis.visualize(node);
            } else if ("prepend".equals(key)) {
                if (node.getState() != ExperimentNode.EXECUTING) {
                    prependNode(value);
                } else JOptionPane.showMessageDialog(Core.frame, "Kann den Knoten während der Berechnung nicht ändern.");
            } else if ("append".equals(key)) {
                this.appendNode(value);
            } else if ("docs".equals(key)) {
                log.debug("open javahelp for id " + value);
                DocBrowser docBrowser = DocBrowser.getInstance();
                docBrowser.jumpTo(value);
                docBrowser.setVisible(true);
            } else {
                try {
                    if (Desktop.isDesktopSupported()) {
                        desktop = Desktop.getDesktop();
                        if (desktop.isSupported(Desktop.Action.BROWSE)) {
                            desktop.browse(e.getURL().toURI());
                        }
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (URISyntaxException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(Core.frame, "Ungültiger URL");
                }
            }
        }
    }
