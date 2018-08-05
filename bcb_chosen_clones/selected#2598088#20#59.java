    public Object loadObject(String clsn, String id) {
        try {
            ClassDef cd = (ClassDef) listCD.get(clsn);
            String qr = gene.getSelect(cd, cd.getPKName() + "=" + id);
            System.out.println(qr);
            Statement stmt = conn.createStatement();
            ResultSet res = stmt.executeQuery(qr);
            if (res == null) return null;
            if (res.next()) {
                Class cls = Class.forName(clsn);
                Class[] cprm = {};
                Object[] pprm = {};
                Object resobj = cls.getConstructor(cprm).newInstance(pprm);
                resobj = fillObj(cd, resobj, res);
                res.close();
                stmt.close();
                return resobj;
            } else {
                res.close();
                stmt.close();
                return null;
            }
        } catch (SQLException e) {
            System.err.println("SQL error trying to load an object of type : " + clsn);
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found during trying to load an object of type : " + clsn);
            e.printStackTrace();
            return null;
        } catch (NoSuchMethodException e) {
            System.err.println("Method not found during trying to load an object of type : " + clsn);
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            System.err.println("Exception during trying to load an object of type : " + clsn);
            e.printStackTrace();
            return null;
        }
    }
