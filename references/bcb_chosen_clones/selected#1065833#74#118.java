    private JMenu buildHelpMenu() {
        JMenu menu = new JMenu("Help");
        JMenuItem websiteItem = new JMenuItem("Website");
        websiteItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String websiteUrl = "http://amun.phpsx.org";
                try {
                    URI websiteUri = new URI(websiteUrl);
                    if (Desktop.isDesktopSupported()) {
                        Desktop desktop = Desktop.getDesktop();
                        if (desktop.isSupported(Desktop.Action.BROWSE)) {
                            desktop.browse(websiteUri);
                        } else {
                            JOptionPane.showMessageDialog(null, websiteUrl);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, websiteUrl);
                    }
                } catch (Exception ex) {
                    Zubat.handleException(ex);
                    JOptionPane.showMessageDialog(null, websiteUrl);
                }
            }
        });
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                StringBuilder out = new StringBuilder();
                out.append("Version: zubat (version: " + Zubat.version + ")\n");
                out.append("Author: Christoph \"k42b3\" Kappestein" + "\n");
                out.append("Website: http://code.google.com/p/delta-quadrant" + "\n");
                out.append("License: GPLv3 <http://www.gnu.org/licenses/gpl-3.0.html>" + "\n");
                out.append("\n");
                out.append("An java application to access the API of Amun (amun.phpsx.org). It is" + "\n");
                out.append("used to debug and control a website based on Amun. This is the reference" + "\n");
                out.append("implementation howto access the API so feel free to hack and extend." + "\n");
                JOptionPane.showMessageDialog(null, out, "About", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        menu.add(websiteItem);
        menu.add(aboutItem);
        return menu;
    }
