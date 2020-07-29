    Doc(MAcctSchema[] ass, Class clazz, ResultSet rs, String defaultDocumentType, String trxName) {
        p_Status = STATUS_Error;
        m_ass = ass;
        m_ctx = new Properties(m_ass[0].getCtx());
        m_ctx.setProperty("#AD_Client_ID", String.valueOf(m_ass[0].getAD_Client_ID()));
        String className = clazz.getName();
        className = className.substring(className.lastIndexOf('.') + 1);
        try {
            Constructor constructor = clazz.getConstructor(new Class[] { Properties.class, ResultSet.class, String.class });
            p_po = (PO) constructor.newInstance(new Object[] { m_ctx, rs, trxName });
        } catch (Exception e) {
            String msg = className + ": " + e.getLocalizedMessage();
            log.severe(msg);
            throw new IllegalArgumentException(msg);
        }
        int index = p_po.get_ColumnIndex("DocStatus");
        if (index != -1) m_DocStatus = (String) p_po.get_Value(index);
        setDocumentType(defaultDocumentType);
        m_trxName = trxName;
        if (m_trxName == null) m_trxName = "Post" + m_DocumentType + p_po.get_ID();
        p_po.set_TrxName(m_trxName);
        m_Amounts[0] = Env.ZERO;
        m_Amounts[1] = Env.ZERO;
        m_Amounts[2] = Env.ZERO;
        m_Amounts[3] = Env.ZERO;
    }
