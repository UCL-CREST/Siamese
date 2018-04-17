    @Override
    public void actionPerformed(ActionEvent e) {
        File dir1 = new File(".");
        StringBuilder b = new StringBuilder();
        b.append("<html>");
        b.append("<table>");
        b.append("<h4><table width='100%'>");
        try {
            String cdir = dir1.getCanonicalPath();
            b.append(String.format("<tr><th>Current directory</th><td><a href='%s/%s'>%s</a></td></tr>", "file://", cdir, cdir));
            b.append(String.format("<tr><th>Plugins directory</th><td><a href='%s/%s'>%s</a></td></tr>", "file://", cdir + "\\ext", cdir + "\\ext"));
            b.append("<tr><th></th><td></td></tr>");
            String[] rr = new String[] { "toxTree.tree.cramer.RuleNormalBodyConstituent", "toxTree.tree.cramer.RuleCommonComponentOfFood", "mutant.descriptors.AromaticAmineSubstituentsDescriptor" };
            for (String clazzname : rr) try {
                RuleStructuresList r = (RuleStructuresList) Introspection.loadCreateObject(clazzname);
                if (r != null) {
                    File f = r.getFile();
                    String msg = (f != null) && f.exists() ? String.format("<a href>%s/%s</a>", "file://", f.getAbsolutePath(), f.getAbsolutePath()) : "Not found";
                    b.append(String.format("<tr><th>File with '%s' compounds</th><td>%s</td></tr>", r.getTitle(), msg));
                }
            } catch (Exception x) {
            }
        } catch (Exception x) {
            b.append(x.getMessage());
        }
        b.append("</table></h4>");
        b.append("</html>");
        JEditorPane label = new JEditorPane("text/html", b.toString());
        label.setBorder(BorderFactory.createEtchedBorder());
        label.setPreferredSize(new Dimension(600, 300));
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
        JOptionPane.showMessageDialog(module.getActions().getFrame(), label, "Files information", JOptionPane.INFORMATION_MESSAGE);
    }
