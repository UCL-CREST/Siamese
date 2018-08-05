    public static Object getRunnableProcessor(Processor processor) {
        Object retobj = null;
        try {
            Class cls = Class.forName(processor.getClassName());
            Class partypes[] = new Class[1];
            partypes[0] = processor.getClass();
            Constructor ct = cls.getConstructor(partypes);
            Object arglist[] = new Object[1];
            arglist[0] = processor;
            retobj = ct.newInstance(arglist);
        } catch (Throwable e) {
            System.err.println(e);
        }
        return retobj;
    }
