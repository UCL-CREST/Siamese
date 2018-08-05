    @Override
    public void actionPerformed(ActionEvent e) {
        Translation t = Translation.getPreferred();
        StringBuilder version = new StringBuilder();
        version.append("<html><h1>Kitchen garden aid</h1><br/>");
        JButton download = null;
        try {
            StringBuffer title = new StringBuffer(), link = new StringBuffer();
            GetLatestVersion(title, link);
            final java.net.URI uri = new java.net.URI(link.toString());
            if (title.indexOf(KitchenGardenAid.VERSION) == -1) {
                version.append("<b>" + t.translate("newversionavailable") + ":</b>" + title);
                download = new JButton(t.translate("gotodownload"));
                download.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (Desktop.isDesktopSupported()) {
                            Desktop desktop = Desktop.getDesktop();
                            try {
                                desktop.browse(uri);
                            } catch (Exception ex) {
                            }
                        }
                    }
                });
            } else version.append("<b>" + t.translate("nonewversionavailable") + ":</b>" + title);
        } catch (Exception ex) {
            version.append(ex.toString());
        }
        JLabel label = new JLabel(version.toString());
        label.setFont(new Font(label.getFont().getName(), Font.PLAIN, label.getFont().getSize()));
        Preferences prefs = Preferences.userRoot().node("/org/sourceforge/kga/checkforupdate");
        JCheckBox checkAuto = new JCheckBox(t.translate("automaticallycheck"), prefs.getBoolean("automatically", true));
        checkAuto.addItemListener(this);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1));
        panel.add(label);
        if (download != null) panel.add(download);
        panel.add(checkAuto);
        JDialog tutorial = new JOptionPane(panel).createDialog(t.translate("checkforupdate"));
        tutorial.setVisible(true);
    }
