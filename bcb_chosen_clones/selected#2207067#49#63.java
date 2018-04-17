    public static int executeNoQuery(String strStatement) throws SQLException {
        MyDBConnection myc = new MyDBConnection();
        myc.init();
        Statement statement = myc.getMyConnection().createStatement();
        try {
            int rows = statement.executeUpdate(strStatement);
            myc.myConnection.commit();
            return rows;
        } catch (SQLException e) {
            myc.myConnection.rollback();
            throw new SQLException("rollback e close effettuato dopo " + e.getMessage());
        } finally {
            myc.close();
        }
    }
