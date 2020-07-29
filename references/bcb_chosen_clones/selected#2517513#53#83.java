    public void showHelp(String id) {
        StringBuilder b = new StringBuilder();
        b.append("<html>");
        b.append("<table>");
        b.append("<table width='100%'>");
        b.append(String.format("<tr><th>WWW</th><td>%s</td></tr>", "<a href='http://toxtree.sf.net'>http://toxtree.sourceforge.net</a>"));
        b.append(String.format("<tr><th>User guide (local)</th><td>%s</td></tr>", "[installation directory]/doc"));
        b.append(String.format("<tr><th>User guide (online)</th><td>%s</td></tr>", "<a href='http://www.ideaconsult.net/resources'>http://www.ideaconsult.net/resources</a>"));
        b.append("</table>");
        b.append("</html>");
        JEditorPane label = new JEditorPane("text/html", b.toString());
        label.setBorder(BorderFactory.createEtchedBorder());
        label.setPreferredSize(new Dimension(400, 300));
        label.setOpaque(false);
        label.setEditable(false);
        label.addHyperlinkListener(new HyperlinkListener() {

            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    try {
                        if (Desktop.isDesktopSupported()) {
                            Desktop.getDesktop().browse(e.getURL().toURI());
                        } else Tools.openURL(e.getURL().toString());
                    } catch (Exception x) {
                        JOptionPane.showMessageDialog(null, x.getMessage());
                    }
                }
            }
        });
        JOptionPane.showMessageDialog(null, label, "Toxtree Help", JOptionPane.INFORMATION_MESSAGE);
    }
