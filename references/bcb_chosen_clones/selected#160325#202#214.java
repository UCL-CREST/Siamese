        @Override
        protected void done() {
            progressBar.setValue(0);
            progressBar.setString("");
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().open(croppedFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
