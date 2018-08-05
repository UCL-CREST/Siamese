    @Override
    public void initialize() throws Exception {
        Properties props = ((PropertiesConfiguration) this.getConfiguration()).getProperties();
        Class cla = Class.forName(props.getProperty("user.database.class"));
        Class[] paramsCla = new Class[2];
        paramsCla[0] = String.class;
        paramsCla[1] = Properties.class;
        Object[] params = new Object[2];
        params[0] = props.getProperty("user.database.id");
        params[1] = props;
        Constructor cons = cla.getConstructor(paramsCla);
        this.database = (UserDatabase) cons.newInstance(params);
    }
