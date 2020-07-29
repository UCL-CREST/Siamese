    public MarshalledObject newInstance(ActivationID id, ActivationDesc desc) throws ActivationException, RemoteException {
        try {
            if (ActivationSystemTransient.debug) System.out.println("Instantiating " + desc.getClassName());
            Remote object;
            Class objectClass;
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            objectClass = loader.loadClass(desc.getClassName());
            Constructor constructor = objectClass.getConstructor(cConstructorTypes);
            object = (Remote) constructor.newInstance(new Object[] { id, desc.getData() });
            ActivatableServerRef ref = UnicastServer.getActivatableRef(id);
            Remote stub = ref.exportObject(object);
            MarshalledObject marsh = new MarshalledObject(stub);
            activeObject(id, marsh);
            activeObject(id, stub);
            return marsh;
        } catch (Exception e) {
            ActivationException acex = new ActivationException("Unable to activate " + desc.getClassName() + " from " + desc.getLocation(), e);
            throw acex;
        }
    }
