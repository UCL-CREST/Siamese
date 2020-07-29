    public Elm2DBGInterface makeBG() {
        try {
            Class c = ElmVE.classLoader.loadClass(className);
            Constructor con = c.getConstructor(new Class[0]);
            Elm2DBGInterface ui = (Elm2DBGInterface) con.newInstance(new Object[0]);
            ui.setElm(elm);
            ui.setPlace(place);
            return ui;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
