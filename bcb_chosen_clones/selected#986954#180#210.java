            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    TreePath tp = arborescence.getClosestPathForLocation(evt.getX(), evt.getY());
                    String path = tp.toString();
                    String name = path.substring(path.indexOf(',') + 2);
                    if (name.contains(" (")) {
                        name = name.substring(0, name.indexOf(" ("));
                    }
                    path = path.substring(path.indexOf(',') + 1, path.length());
                    path = path.replaceAll("\\([^,]*,", "/");
                    path = path.replaceAll(",", "/");
                    path = path.replaceAll(" ", "");
                    path = path.replaceAll("\\]", "");
                    Site site = siteListH.getList().getSite(name);
                    if (site != null) {
                        path = site.getTarget() + "\\" + path;
                    }
                    try {
                        File file = new File(path);
                        if (file.isFile()) {
                            if (Desktop.isDesktopSupported()) {
                                Desktop desktop = Desktop.getDesktop();
                                if (desktop.isSupported(Desktop.Action.OPEN)) {
                                    desktop.open(file);
                                }
                            }
                        }
                    } catch (Exception rex) {
                    }
                }
            }
