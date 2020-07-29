    @Override
    public void actionPerformed(ActionEvent e) {
        StringBuffer b = new StringBuffer();
        b.append("<html>");
        b.append("<table width='100%'>");
        Package self = Package.getPackage(packageName);
        b.append(String.format("<tr><th>Title</th><td>%s</td></tr>", self.getImplementationTitle()));
        b.append(String.format("<tr><th>Vendor</th><td>%s</td></tr>", self.getImplementationVendor()));
        b.append(String.format("<tr><th>Version</th><td>%s</td></tr>", self.getImplementationVersion()));
        b.append("</table>");
        if (packageName.equals("toxTree.apps")) {
            self = Package.getPackage("toxTree.tree.cramer");
            if (self != null) {
                b.append("<h5>Built-in decision tree:\n");
                String s = self.getSpecificationTitle();
                if (s == null) s = "Cramer scheme as in \"Cramer G. M., R. A. Ford, R. L. Hall, Estimation of Toxic Hazard - A Decision Tree Approach,J. Cosmet. Toxicol.,Vol.16, pp. 255-276, Pergamon Press, 1978\"";
                b.append(s);
                b.append("<br>Version: ");
                b.append(self.getImplementationVersion());
            }
        }
        b.append("<br>");
        b.append("<table width='100%'>");
        b.append(String.format("<tr><th>WWW</th><td>%s</td></tr>", "<a href='http://toxtree.sf.net'>http://toxtree.sourceforge.net</a>"));
        b.append(String.format("<tr><th>Support:</th><td>%s</td></tr>", "<a a href='http://toxtree.sourceforge.net/issue-tracking.html'>Issue tracking</a></a>"));
        b.append(String.format("<tr><th></th><td>%s</td></tr>", "<a href='http://www.ideaconsult.net'>http://www.ideaconsult.net</a>"));
        b.append(String.format("<tr><th></th><td>%s</td></tr>", "<a href='http://ihcp.jrc.ec.europa.eu/our_labs/computational_toxicology/qsar_tools/toxtree'>JRC Computational Toxicology</a>"));
        b.append(String.format("<tr><th></th><td>%s</td></tr>", "<a href='mailto:jeliazkova.nina@gmail.com'>e-mail</a>"));
        b.append("</table>");
        b.append("</html>");
        ImageIcon toxTreeIcon = null;
        try {
            toxTreeIcon = Tools.getImage("bird.gif");
        } catch (Exception x) {
            toxTreeIcon = null;
        }
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
                        JOptionPane.showMessageDialog(module.getActions().getFrame(), x.getMessage());
                    }
                }
            }
        });
        JOptionPane.showMessageDialog(module.getActions().getFrame(), label, "About", JOptionPane.INFORMATION_MESSAGE, toxTreeIcon);
    }
