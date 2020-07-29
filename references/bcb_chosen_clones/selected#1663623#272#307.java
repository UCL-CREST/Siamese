    @Override
    public void insert(Connection conn) throws SQLException {
        PreparedStatement objectInsert = null;
        String sqlString = null;
        int newID = 0;
        try {
            conn.setAutoCommit(false);
            sqlString = "SELECT NEXTVAL(OBJ_SEQ) AS NEXTVAL";
            objectInsert = conn.prepareStatement(sqlString);
            ResultSet r = objectInsert.executeQuery(sqlString);
            newID = r.getInt("NEXTVAL");
            sqlString = "INSERT INTO OBJECTS" + "(" + "OBJ_ID," + "OBJ_NAME," + "OBTY_CDE" + ")" + "VALUES" + "(" + "?," + "?," + "?" + ")" + "";
            objectInsert = conn.prepareStatement(sqlString);
            objectInsert.setInt(1, newID);
            objectInsert.setString(2, getRoomKey());
            objectInsert.setString(3, "ROOM");
            objectInsert.executeUpdate();
            sqlString = "INSERT INTO ROOMS" + "(" + "";
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    System.err.print("Transaction is being rolled back");
                    conn.rollback();
                } catch (SQLException excep) {
                    excep.printStackTrace();
                }
            }
        } finally {
            if (objectInsert != null) {
                objectInsert.close();
            }
            conn.setAutoCommit(true);
        }
    }
