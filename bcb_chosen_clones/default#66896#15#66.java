    public SuopfAgent(String code) {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/kato?user=dbuser&password=dbpassword");
            stmt = conn.createStatement();
            String query = "select s.ScheduleID from Schedule s inner join Catalog c on s.CatalogID = c.CatalogID where c.Code = '" + code + "' and (s.Finished = 0 or s.Finished is null) and Occured >= curdate() order by Occured";
            rs = stmt.executeQuery(query);
            int scheduleID = 0;
            if (rs.next()) {
                scheduleID = rs.getInt(1);
            }
            rs.close();
            if (scheduleID > 0) {
                id = new Integer(scheduleID).toString();
                query = "select pn.Code as ParamName, pv.Code as ParamValue from ScheduleParameter sp inner join ParameterValue pv on sp.ParameterValueID = pv.ParameterValueID inner join ParameterName pn on pv.ParameterNameID = pn.ParameterNameID where sp.ScheduleID = " + id + " union select KeyCode as ParamName, Value as ParamValue from ScheduleData where ScheduleID = " + id;
                rs = stmt.executeQuery(query);
                while (rs.next()) {
                    String name = rs.getString("ParamName");
                    String value = rs.getString("ParamValue");
                    setParameter(name, value);
                }
                rs.close();
                String sql = "update Schedule set Occured = adddate(Occured, interval 1 day) where ScheduleID = " + id;
                stmt.execute(sql);
                activated = true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                }
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                }
                stmt = null;
            }
        }
    }
