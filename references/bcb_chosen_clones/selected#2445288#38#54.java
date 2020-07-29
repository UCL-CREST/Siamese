    public Object nextElement() {
        try {
            byte option = buffer.get(buffer.position());
            Class optionClass = getOptionClass(option);
            if (optionClass == null) {
                System.err.println("Option " + option + " not supported!");
                return null;
            }
            Class[] args = { ByteBuffer.class };
            java.lang.reflect.Constructor cons = optionClass.getConstructor(args);
            Object[] arg = { buffer };
            return cons.newInstance(arg);
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }
