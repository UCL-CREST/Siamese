            public void onClickDo(final MouseEvent me) {
                if (Desktop.isDesktopSupported()) {
                    final Desktop desktop = Desktop.getDesktop();
                    try {
                        desktop.browse(new URI("http://eworld.sourceforge.net"));
                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                }
            }
