    public static <T extends XdrAble> T deserialize(XdrDecodingStream ds, Class<T> clazz) {
        assert ds != null : "ds is null";
        assert clazz != null : "clazz is null";
        T xdrAble = null;
        try {
            ds.beginDecoding();
            xdrAble = clazz.getConstructor(new Class[] { XdrDecodingStream.class }).newInstance(new Object[] { ds });
        } catch (NoSuchMethodException nsme) {
            try {
                xdrAble = clazz.newInstance();
                xdrAble.xdrDecode(ds);
            } catch (Exception e) {
                assert false : " error while deserializing primitive " + clazz.getName() + ". " + e;
            }
        } catch (Exception e) {
            assert false : " error while deserializing " + clazz.getName() + ". " + e;
        }
        return xdrAble;
    }
