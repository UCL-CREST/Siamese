    public AdminCommand getCommand(String name, Locale locale) {
        try {
            Class commandType = Class.forName(commands.get(name).toString());
            Constructor ctor = commandType.getConstructor((Class[]) null);
            return (AdminCommand) ctor.newInstance((Object[]) null);
        } catch (Exception ex) {
            return null;
        }
    }
