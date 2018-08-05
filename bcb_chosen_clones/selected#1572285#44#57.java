    public void mouseClicked(MouseEvent e) {
        HyperlinkLabel self = (HyperlinkLabel) e.getSource();
        if (self.url == null) return;
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) try {
                desktop.browse(url.toURI());
                return;
            } catch (Exception exp) {
            }
        }
        JOptionPane.showMessageDialog(this, "Cannot launch browser...\n Please, visit\n" + url.getRef(), "", JOptionPane.INFORMATION_MESSAGE);
        return;
    }
