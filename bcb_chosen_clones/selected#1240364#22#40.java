    public Object stringToValue(String str, ItsNatFormattedTextField comp) throws ParseException {
        Class vc = getValueClass(comp);
        if (vc != null) {
            Constructor cons;
            try {
                cons = vc.getConstructor(new Class[] { String.class });
            } catch (NoSuchMethodException nsme) {
                cons = null;
            }
            if (cons != null) {
                try {
                    return cons.newInstance(new Object[] { str });
                } catch (Exception ex) {
                    throw new ParseException("Error creating instance", 0);
                }
            }
        }
        return str;
    }
