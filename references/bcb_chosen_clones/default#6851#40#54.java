    public static BigDecimal ODCITableStart(STRUCT[] sctx, ResultSet rset) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:default:connection:");
        StoredCtx ctx = new StoredCtx(rset);
        int key;
        try {
            key = ContextManager.setContext(ctx);
        } catch (CountException ce) {
            return ERROR;
        }
        Object[] impAttr = new Object[1];
        impAttr[0] = new BigDecimal(key);
        StructDescriptor sd = new StructDescriptor("JdbmsCompressZipEntryImp", conn);
        sctx[0] = new STRUCT(sd, conn, impAttr);
        return SUCCESS;
    }
