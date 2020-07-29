    public static boolean helpToBrowser(JFrame ss) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                int create = JOptionPane.showConfirmDialog(ss, Jibs.getString("SortScreen.10") + "\n" + Jibs.getString("SortScreen.52"), Jibs.getString("SortScreen.51"), JOptionPane.YES_NO_OPTION);
                if (create == JOptionPane.YES_OPTION) {
                    desktop = Desktop.getDesktop();
                    try {
                        desktop.browse(new URI("http://www.jibs.us/"));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                return true;
            }
            return false;
        }
        return false;
    }
