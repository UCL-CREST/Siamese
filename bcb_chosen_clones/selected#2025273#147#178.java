    public void startApp(final FudaaCommonImplementation _impl, final Class _application) {
        if (splash_ != null) {
            splash_.setProgression(60);
        }
        BuApplication app = null;
        if (_application == null) {
            app = new BuApplication();
        } else {
            try {
                app = (BuApplication) _application.getConstructors()[0].newInstance(new Object[0]);
            } catch (final Exception ex) {
                throw new IllegalArgumentException("application n'est pas une BuApplication");
            }
        }
        app.setImplementation(_impl);
        final String s = FudaaLib.getS("Initialisation") + "...";
        System.out.println(s);
        if (splash_ != null) {
            splash_.setText(s);
            splash_.setProgression(80);
        }
        app.init();
        if (splash_ != null) {
            splash_.setProgression(100);
            splash_.setVisible(false);
            splash_.dispose();
        }
        app.start();
        if (toOpen_ != null) {
            _impl.cmdOuvrirFile(toOpen_);
        }
    }
