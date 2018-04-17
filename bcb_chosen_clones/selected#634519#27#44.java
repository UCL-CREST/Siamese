    public SObject() {
        for (Class<?> c : this.getClass().getClasses()) try {
            if (ISignal.class.isAssignableFrom(c) && c.getConstructor(getClass(), SObject.class) != null) {
                ISignal s = (ISignal) c.getConstructor(getClass(), SObject.class).newInstance(this, this);
                signals.put(s.getName(), s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Method m : getSignalMethods(this)) {
            AbstractSignal s = new MethodSignal(this, m);
            signals.put(s.getName(), s);
        }
        for (Method m : getSinkMethods(this)) {
            Sink s = new Sink(this, m);
            sinks.put(s.getName(), s);
        }
    }
