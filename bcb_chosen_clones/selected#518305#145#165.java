    public static Record fromString(Name name, short type, short dclass, int ttl, MyStringTokenizer st, Name origin) throws IOException {
        Record rec;
        try {
            Class rrclass;
            Constructor m;
            rrclass = toClass(type);
            m = rrclass.getConstructor(new Class[] { Name.class, Short.TYPE, Integer.TYPE, MyStringTokenizer.class, Name.class });
            rec = (Record) m.newInstance(new Object[] { name, new Short(dclass), new Integer(ttl), st, origin });
            return rec;
        } catch (ClassNotFoundException e) {
            rec = new UNKRecord(name, type, dclass, ttl, st, origin);
            return rec;
        } catch (InvocationTargetException e) {
            System.out.println("from text: " + e);
            System.out.println(e.getTargetException());
            return null;
        } catch (Exception e) {
            System.out.println("from text: " + e);
            return null;
        }
    }
