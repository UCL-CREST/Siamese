    private static CommandRep createCommandRepForType(final String name, final Properties props, final String type) {
        if (null == commandRepMap.get(type)) {
            throw new IllegalArgumentException("can't create command for unrecognized type: " + type);
        }
        Class repcls = (Class) commandRepMap.get(type);
        try {
            Constructor c = repcls.getConstructor(new Class[] { String.class, Properties.class });
            Object o = c.newInstance(new Object[] { name, props });
            if (o instanceof CommandRep) {
                return (CommandRep) o;
            } else {
                throw new AntdepoException("Error Creating CommandRep: not a CommandRep class: " + o.getClass().getName());
            }
        } catch (NoSuchMethodException e) {
            throw new AntdepoException("Error Creating CommandRep:" + e, e);
        } catch (InstantiationException e) {
            throw new AntdepoException("Error Creating CommandRep:" + e, e);
        } catch (IllegalAccessException e) {
            throw new AntdepoException("Error Creating CommandRep:" + e, e);
        } catch (InvocationTargetException e) {
            throw new AntdepoException("Error Creating CommandRep:" + e, e);
        }
    }
