    public Message createMessage(String name) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        if (name == null) {
            throw new NullPointerException("Message name not specified.");
        }
        Constructor con = getConstructor(GenesysPropertyManager.getInstance().getMessagePackage() + "." + name);
        Message msg = (Message) con.newInstance(new Object[] { name });
        return msg;
    }
