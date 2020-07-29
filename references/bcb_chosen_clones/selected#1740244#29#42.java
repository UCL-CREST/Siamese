            @Override
            public void open(OpenEvent event) {
                IStructuredSelection selection = (IStructuredSelection) event.getSelection();
                File file = (File) selection.getFirstElement();
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    if (desktop.isSupported(Desktop.Action.OPEN)) {
                        try {
                            desktop.open(file);
                        } catch (IOException e) {
                        }
                    }
                }
            }
