    public static void main(String[] args) {
        Frame splashFrame = null;
        splashFrame = SplashLoader.splash(ImageManager.getInstance().getImage(ImageManager.SPLASH));
        try {
            Class.forName("de.fuhrmeister.browserchooser.Loader").getMethod("main", new Class[] { String[].class }).invoke(null, new Object[] { args });
        } catch (final Exception ex) {
            ex.printStackTrace();
            System.exit(10);
        }
        if (splashFrame != null) {
            splashFrame.dispose();
        }
    }
