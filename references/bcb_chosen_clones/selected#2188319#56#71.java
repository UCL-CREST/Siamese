    public Test getTestProxy(Test test) {
        InvocationHandler handler = new RunningTestProxy(this, test);
        Class cl = test.getClass();
        if (test instanceof JFuncAssert) {
            ((JFuncAssert) test).setResult(this);
        }
        try {
            Class proxy = TestletWrapper.getProxy(new Class[0], cl);
            Constructor cons = proxy.getConstructor(new Class[] { InvocationHandler.class });
            return (Test) cons.newInstance(new Object[] { handler });
        } catch (InstantiationException ie) {
            throw new RuntimeException(ie.toString());
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }
    }
