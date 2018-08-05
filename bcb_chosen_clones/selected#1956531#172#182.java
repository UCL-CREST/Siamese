    public void addMessageListener(String classname) {
        try {
            Class<?> clazz = Class.forName(classname);
            Constructor cstr = clazz.getConstructor(Server.class);
            MessageListener listener = (MessageListener) cstr.newInstance(this);
            getNotifier().addListener(listener);
        } catch (Exception ex) {
            System.err.println("Failed adding MessageListener:  " + ex.toString());
            ex.printStackTrace();
        }
    }
