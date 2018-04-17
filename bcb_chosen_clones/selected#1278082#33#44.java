                @Override
                public void hyperlinkUpdate(HyperlinkEvent hlinkEvt) {
                    try {
                        if (hlinkEvt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                            if (!Desktop.isDesktopSupported()) throw new Exception("Cannot open link: this system does not support opening web links.");
                            Desktop desktop = Desktop.getDesktop();
                            desktop.browse(hlinkEvt.getURL().toURI());
                        }
                    } catch (Throwable e) {
                        ErrDialog.errorDialog(getContentPane(), ErrUtils.getExceptionMessage(e));
                    }
                }
