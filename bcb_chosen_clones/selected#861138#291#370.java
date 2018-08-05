    public void downSync(Vector v) throws SQLException {
        try {
            con = allocateConnection(tableName);
            PreparedStatement update = con.prepareStatement("update cal_Event set owner=?,subject=?,text=?,place=?," + "contactperson=?,startdate=?,enddate=?,starttime=?,endtime=?,allday=?," + "syncstatus=?,dirtybits=? where OId=? and syncstatus=?");
            PreparedStatement insert = con.prepareStatement("insert into cal_Event (owner,subject,text,place," + "contactperson,startdate,enddate,starttime,endtime,allday,syncstatus," + "dirtybits) values(?,?,?,?,?,?,?,?,?,?,?,?)");
            PreparedStatement insert1 = con.prepareStatement(DBUtil.getQueryCurrentOID(con, "cal_Event", "newoid"));
            PreparedStatement delete1 = con.prepareStatement("delete from  cal_Event_Remind where event=?");
            PreparedStatement delete2 = con.prepareStatement("delete from  cal_Event where OId=? " + "and (syncstatus=? or syncstatus=?)");
            for (int i = 0; i < v.size(); i++) {
                try {
                    DO = (EventDO) v.elementAt(i);
                    if (DO.getSyncstatus() == INSERT) {
                        insert.setBigDecimal(1, DO.getOwner());
                        insert.setString(2, DO.getSubject());
                        insert.setString(3, DO.getText());
                        insert.setString(4, DO.getPlace());
                        insert.setString(5, DO.getContactperson());
                        insert.setDate(6, DO.getStartdate());
                        insert.setDate(7, DO.getEnddate());
                        insert.setTime(8, DO.getStarttime());
                        insert.setTime(9, DO.getEndtime());
                        insert.setBoolean(10, DO.getAllday());
                        insert.setInt(11, RESET);
                        insert.setInt(12, RESET);
                        con.executeUpdate(insert, null);
                        con.reset();
                        rs = con.executeQuery(insert1, null);
                        if (rs.next()) DO.setOId(rs.getBigDecimal("newoid"));
                        con.reset();
                    } else if (DO.getSyncstatus() == UPDATE) {
                        update.setBigDecimal(1, DO.getOwner());
                        update.setString(2, DO.getSubject());
                        update.setString(3, DO.getText());
                        update.setString(4, DO.getPlace());
                        update.setString(5, DO.getContactperson());
                        update.setDate(6, DO.getStartdate());
                        update.setDate(7, DO.getEnddate());
                        update.setTime(8, DO.getStarttime());
                        update.setTime(9, DO.getEndtime());
                        update.setBoolean(10, DO.getAllday());
                        update.setInt(11, RESET);
                        update.setInt(12, RESET);
                        update.setBigDecimal(13, DO.getOId());
                        update.setInt(14, RESET);
                        con.executeUpdate(update, null);
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
                            } else {
                                con.commit();
                            }
                        } catch (Exception e) {
                            con.rollback();
                            throw e;
                        } finally {
                            con.reset();
                        }
                    }
                } catch (Exception e) {
                    if (DO != null) logError("Sync-EventDO.owner = " + DO.getOwner().toString() + " oid = " + (DO.getOId() != null ? DO.getOId().toString() : "NULL"), e);
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
    }
