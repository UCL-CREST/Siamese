    private Object createInetSocketAddress(String host, int port) throws NoSuchMethodException {
        try {
            Class inetSocketAddressClass = Class.forName("java.net.InetSocketAddress");
            Constructor inetSocketAddressCons = inetSocketAddressClass.getConstructor(new Class[] { String.class, int.class });
            return inetSocketAddressCons.newInstance(new Object[] { host, new Integer(port) });
        } catch (ClassNotFoundException e) {
            throw new NoSuchMethodException();
        } catch (InstantiationException e) {
            throw new NoSuchMethodException();
        } catch (InvocationTargetException e) {
            throw new NoSuchMethodException();
        } catch (IllegalAccessException e) {
            throw new NoSuchMethodException();
        }
    }
