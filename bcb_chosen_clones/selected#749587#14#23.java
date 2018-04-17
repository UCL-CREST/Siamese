    public Servant incarnate(byte[] oid, POA adapter) throws ForwardRequest {
        try {
            System.out.println("[" + DateFormat.getDateTimeInstance().format(new Date()) + "]  " + "connected " + adapter.the_name());
            Class cl = Class.forName(m_sPackageName + adapter.the_name().replace("POA", "Impl"));
            return (Servant) cl.getConstructor(new Class[] { oid.getClass() }).newInstance(new Object[] { oid });
        } catch (Exception e) {
            System.err.println("preinvoke: Caught exception - " + e);
        }
        return null;
    }
