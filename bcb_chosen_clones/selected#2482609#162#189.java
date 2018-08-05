    private SemanticRecorder getSemanticRecorder(Class cls) {
        if (!(Component.class.isAssignableFrom(cls))) {
            throw new IllegalArgumentException("Class must derive from " + "Component");
        }
        SemanticRecorder sr = (SemanticRecorder) semanticRecorders.get(cls);
        if (sr == null) {
            String cname = simpleClassName(cls);
            try {
                String pkg = SemanticRecorder.class.getPackage().getName();
                cname = pkg + "." + cname + "Recorder";
                Class recorderClass = Class.forName(cname);
                Constructor ctor = recorderClass.getConstructor(new Class[] { Resolver.class, ComponentFinder.class, ActionListener.class });
                sr = (SemanticRecorder) ctor.newInstance(new Object[] { getResolver(), getFinder(), getListener() });
                semanticRecorders.put(cls, sr);
            } catch (InvocationTargetException e) {
                Log.warn(e);
            } catch (NoSuchMethodException e) {
                sr = getSemanticRecorder(cls.getSuperclass());
            } catch (InstantiationException e) {
                sr = getSemanticRecorder(cls.getSuperclass());
            } catch (IllegalAccessException iae) {
                sr = getSemanticRecorder(cls.getSuperclass());
            } catch (ClassNotFoundException cnf) {
                sr = getSemanticRecorder(cls.getSuperclass());
            }
        }
        return sr;
    }
