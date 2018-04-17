    public Vector downSync(Vector v) throws SQLException {
        Vector retVector = new Vector();
        try {
            con = allocateConnection(tableName);
            PreparedStatement update = con.prepareStatement("update cont_Contact set owner=?,firstname=?," + "lastname=?,nickname=?,title=?,organization=?,orgunit=?," + "emailaddr=?,homeph=?,workph=?,cellph=?,im=?,imno=?," + "fax=?,homeaddr=?,homelocality=?,homeregion=?," + "homepcode=?,homecountry=?,workaddr=?,worklocality=?," + "workregion=?,workpcode=?,workcountry=?,website=?," + "wapsite=?,comments=?,birthday=?,syncstatus=?,dirtybits=? " + "where OId=? and syncstatus=?");
            PreparedStatement insert = con.prepareStatement("insert into cont_Contact (owner,firstname,lastname," + "nickname,title,organization,orgunit,emailaddr,homeph," + "workph,cellph,im,imno,fax,homeaddr,homelocality," + "homeregion,homepcode,homecountry,workaddr,worklocality," + "workregion,workpcode,workcountry,website,wapsite," + "comments,birthday,syncstatus,dirtybits,quicklist) " + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?)");
            PreparedStatement insert1 = con.prepareStatement(DBUtil.getQueryCurrentOID(con, "cont_Contact", "newoid"));
            PreparedStatement delete1 = con.prepareStatement("delete from  cont_Contact_Group_Rel where externalcontact=?");
            PreparedStatement delete2 = con.prepareStatement("delete from  cont_Contact where OId=? " + "and (syncstatus=? or syncstatus=?)");
            for (int i = 0; i < v.size(); i++) {
                try {
                    DO = (ExternalContactDO) v.elementAt(i);
                    if (DO.getSyncstatus() == INSERT) {
                        insert.setBigDecimal(1, DO.getOwner());
                        insert.setString(2, DO.getFirstname());
                        insert.setString(3, DO.getLastname());
                        insert.setString(4, DO.getNickname());
                        insert.setString(5, DO.getTitle());
                        insert.setString(6, DO.getOrganization());
                        insert.setString(7, DO.getOrgunit());
                        insert.setString(8, DO.getEmail());
                        insert.setString(9, DO.getHomeph());
                        insert.setString(10, DO.getWorkph());
                        insert.setString(11, DO.getCellph());
                        insert.setString(12, DO.getIm());
                        insert.setString(13, DO.getImno());
                        insert.setString(14, DO.getFax());
                        insert.setString(15, DO.getHomeaddr());
                        insert.setString(16, DO.getHomelocality());
                        insert.setString(17, DO.getHomeregion());
                        insert.setString(18, DO.getHomepcode());
                        insert.setString(19, DO.getHomecountry());
                        insert.setString(20, DO.getWorkaddr());
                        insert.setString(21, DO.getWorklocality());
                        insert.setString(22, DO.getWorkregion());
                        insert.setString(23, DO.getWorkpcode());
                        insert.setString(24, DO.getWorkcountry());
                        insert.setString(25, DO.getWebsite());
                        insert.setString(26, DO.getWapsite());
                        insert.setString(27, DO.getComments());
                        if (DO.getBirthday() != null) insert.setDate(28, DO.getBirthday()); else insert.setNull(28, Types.DATE);
                        insert.setInt(29, RESET);
                        insert.setInt(30, RESET);
                        insert.setInt(31, 0);
                        con.executeUpdate(insert, null);
                        con.reset();
                        rs = con.executeQuery(insert1, null);
                        if (rs.next()) DO.setOId(rs.getBigDecimal("newoid"));
                        con.reset();
                        retVector.add(DO);
                    } else if (DO.getSyncstatus() == UPDATE) {
                        update.setBigDecimal(1, DO.getOwner());
                        update.setString(2, DO.getFirstname());
                        update.setString(3, DO.getLastname());
                        update.setString(4, DO.getNickname());
                        update.setString(5, DO.getTitle());
                        update.setString(6, DO.getOrganization());
                        update.setString(7, DO.getOrgunit());
                        update.setString(8, DO.getEmail());
                        update.setString(9, DO.getHomeph());
                        update.setString(10, DO.getWorkph());
                        update.setString(11, DO.getCellph());
                        update.setString(12, DO.getIm());
                        update.setString(13, DO.getImno());
                        update.setString(14, DO.getFax());
                        update.setString(15, DO.getHomeaddr());
                        update.setString(16, DO.getHomelocality());
                        update.setString(17, DO.getHomeregion());
                        update.setString(18, DO.getHomepcode());
                        update.setString(19, DO.getHomecountry());
                        update.setString(20, DO.getWorkaddr());
                        update.setString(21, DO.getWorklocality());
                        update.setString(22, DO.getWorkregion());
                        update.setString(23, DO.getWorkpcode());
                        update.setString(24, DO.getWorkcountry());
                        update.setString(25, DO.getWebsite());
                        update.setString(26, DO.getWapsite());
                        update.setString(27, DO.getComments());
                        if (DO.getBirthday() != null) update.setDate(28, DO.getBirthday()); else update.setNull(28, Types.DATE);
                        update.setInt(29, RESET);
                        update.setInt(30, RESET);
                        update.setBigDecimal(31, DO.getOId());
                        update.setInt(32, RESET);
                        if (con.executeUpdate(update, null) < 1) retVector.add(DO);
                        con.reset();
                    } else if (DO.getSyncstatus() == DELETE) {
                        try {
                            con.setAutoCommit(false);
                            delete1.setBigDecimal(1, DO.getOId());
                            con.executeUpdate(delete1, null);
                            delete2.setBigDecimal(1, DO.getOId());
                            delete2.setInt(2, RESET);
                            delete2.setInt(3, DELETE);
                            if (con.executeUpdate(delete2, null) < 1) {
                                con.rollback();
                                retVector.add(DO);
                            } else {
                                con.commit();
                            }
                        } catch (Exception e) {
                            con.rollback();
                            retVector.add(DO);
                            throw e;
                        } finally {
                            con.reset();
                        }
                    }
                } catch (Exception e) {
                    if (DO != null) logError("Sync-ExternalContactDO.owner = " + DO.getOwner().toString() + " oid = " + (DO.getOId() != null ? DO.getOId().toString() : "NULL"), e);
                }
            }
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            if (DEBUG) logError("", e);
            throw e;
        } finally {
            release();
        }
        return retVector;
    }
