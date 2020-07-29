    public BigDecimal ODCITableFetch(BigDecimal nrows, ARRAY[] outSet) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:default:connection:");
        StoredCtx ctx;
        try {
            ctx = (StoredCtx) ContextManager.getContext(key.intValue());
        } catch (InvalidKeyException ik) {
            return ERROR;
        }
        int nrowsval = nrows.intValue();
        if (nrowsval > 10) nrowsval = 10;
        Vector v = new Vector(nrowsval);
        int i = 0;
        StructDescriptor outDesc = StructDescriptor.createDescriptor("ZIPENTRYTYPE", conn);
        Object[] out_attr = new Object[3];
        while (nrowsval > 0 && ctx.rset.next()) {
            out_attr[0] = (Object) ctx.rset.getString(1);
            out_attr[1] = (Object) new String("O");
            out_attr[2] = (Object) new BigDecimal(ctx.rset.getFloat(2));
            v.add((Object) new STRUCT(outDesc, conn, out_attr));
            out_attr[1] = (Object) new String("C");
            out_attr[2] = (Object) new BigDecimal(ctx.rset.getFloat(3));
            v.add((Object) new STRUCT(outDesc, conn, out_attr));
            i += 2;
            nrowsval -= 2;
        }
        if (i == 0) return SUCCESS;
        Object out_arr[] = v.toArray();
        ArrayDescriptor ad = new ArrayDescriptor("ZIPENTRYTYPESET", conn);
        outSet[0] = new ARRAY(ad, conn, out_arr);
        return SUCCESS;
    }
