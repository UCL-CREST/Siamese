    static Record newRecord(Name name, short type, short dclass, int ttl, int length, CountedDataInputStream in, Compression c) throws IOException {
        Record rec;
        try {
            Class rrclass;
            Constructor m;
            rrclass = toClass(type);
            m = rrclass.getConstructor(new Class[] { Name.class, Short.TYPE, Integer.TYPE, Integer.TYPE, CountedDataInputStream.class, Compression.class });
            rec = (Record) m.newInstance(new Object[] { name, new Short(dclass), new Integer(ttl), new Integer(length), in, c });
            return rec;
        } catch (ClassNotFoundException e) {
            rec = new UNKRecord(name, type, dclass, ttl, length, in, c);
            rec.oLength = length;
            return rec;
        } catch (InvocationTargetException e) {
            System.out.println("new record: " + e);
            System.out.println(e.getTargetException());
            return null;
        } catch (Exception e) {
            System.out.println("new record: " + e);
            return null;
        }
    }
