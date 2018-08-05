    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            RssItem item = (RssItem) listNews.getSelectedValue();
            Desktop desktop;
            if (Desktop.isDesktopSupported()) {
                desktop = Desktop.getDesktop();
                try {
                    desktop.browse(new URI(item.getUrl()));
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
