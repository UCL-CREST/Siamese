    protected Object parseOption(Option opt, String arg) throws BadArgsException {
        Constructor init;
        Object[] param;
        Class[] sig;
        try {
            sig = new Class[] { arg.getClass() };
            param = new Object[] { arg };
            init = opt.type_.getConstructor(sig);
            return init.newInstance(param);
        } catch (Exception e) {
            opt.help(System.err);
            throw new BadArgsException("Can't assign \"" + arg + "\" to option \"" + opt.name_ + "\"");
        }
    }
