    private void insertService(String table, int type) {
        Connection con = null;
        log.info("");
        log.info("正在生成" + table + "的服务。。。。。。。");
        try {
            con = DODataSource.getDefaultCon();
            con.setAutoCommit(false);
            Statement stmt = con.createStatement();
            Statement stmt2 = con.createStatement();
            String serviceUid = UUIDHex.getInstance().generate();
            DOBO bo = DOBO.getDOBOByName(table);
            List props = new ArrayList();
            StringBuffer mainSql = null;
            String name = "";
            String l10n = "";
            String prefix = table;
            String serviceType = "null";
            Boolean isNew = null;
            switch(type) {
                case 1:
                    name = prefix + "_insert";
                    l10n = name;
                    props = bo.retrieveProperties();
                    mainSql = getInsertSql(props, table);
                    serviceType = "8";
                    isNew = Boolean.TRUE;
                    break;
                case 2:
                    name = prefix + "_update";
                    l10n = name;
                    props = bo.retrieveProperties();
                    mainSql = this.getModiSql(props, table);
                    serviceType = "7";
                    isNew = Boolean.FALSE;
                    break;
                case 3:
                    DOBOProperty property = DOBOProperty.getDOBOPropertyByName(bo.getName(), this.keyCol);
                    if (property == null || property.getColName() == null) {
                        return;
                    }
                    name = prefix + "_delete";
                    l10n = name;
                    props.add(property);
                    mainSql = new StringBuffer("delete from ").append(table).append(" where ").append(this.keyCol).append(" = ?");
                    serviceType = "5";
                    break;
                case 4:
                    property = DOBOProperty.getDOBOPropertyByName(bo.getName(), this.keyCol);
                    if (property == null || property.getColName() == null) {
                        return;
                    }
                    name = prefix + "_browse";
                    l10n = name;
                    props.add(property);
                    mainSql = new StringBuffer("select * from ").append(table).append(" where ").append(this.keyCol).append(" = ?");
                    serviceType = "10";
                    break;
                case 5:
                    serviceType = "2";
                    name = prefix + "_list";
                    l10n = name;
                    mainSql = new StringBuffer("select * from ").append(table);
            }
            this.setParaLinkBatch(props, stmt2, serviceUid, isNew);
            StringBuffer aSql = new StringBuffer("insert into DO_Service(objuid,l10n,name,bouid,mainSql,type) values(").append("'").append(serviceUid).append("','").append(l10n).append("','").append(name).append("','").append(this.getDOBOUid(table)).append("','").append(mainSql).append("',").append(serviceType).append(")");
            log.info("Servcice's Sql:" + aSql.toString());
            stmt.executeUpdate(aSql.toString());
            stmt2.executeBatch();
            con.commit();
        } catch (SQLException ex) {
            try {
                con.rollback();
            } catch (SQLException ex2) {
                ex2.printStackTrace();
            }
            ex.printStackTrace();
        } finally {
            try {
                if (!con.isClosed()) {
                    con.close();
                }
            } catch (SQLException ex1) {
                ex1.printStackTrace();
            }
        }
    }
