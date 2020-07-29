    protected void open(File file) throws Exception {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
            File fixedFile = new File(file.getAbsoluteFile().toString()) {

                public URI toURI() {
                    try {
                        return new URI("file://" + getAbsolutePath());
                    } catch (Exception e) {
                        return super.toURI();
                    }
                }
            };
            Desktop.getDesktop().open(fixedFile);
        }
    }
