    public Elm2DUIInterface makeUI() {
        try {
            Class c = ElmVE.classLoader.loadClass(className);
            Constructor con = c.getConstructor(new Class[0]);
            Elm2DUIInterface ui = (Elm2DUIInterface) con.newInstance(new Object[0]);
            ui.setElm(elm);
            ui.setPlace(place);
            return ui;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
