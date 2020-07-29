    public boolean setSchedule(Schedule s) {
        PreparedStatement pst1 = null;
        PreparedStatement pst2 = null;
        PreparedStatement pst3 = null;
        ResultSet rs2 = null;
        boolean retVal = true;
        try {
            conn = getConnection();
            pst1 = conn.prepareStatement("INSERT INTO timetable (recipe_id, time, meal) VALUES (?, ?, ?);");
            pst2 = conn.prepareStatement("SELECT * FROM timetable WHERE time BETWEEN ? AND ?");
            pst3 = conn.prepareStatement("DELETE FROM timetable WHERE time = ? AND meal = ? AND recipe_id = ?");
            long dateInMillis = s.getDate().getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");
            Date beginDate = null, endDate = null;
            try {
                String temp = sdf.format(new java.util.Date(dateInMillis));
                sdf.applyPattern("yyyy-MM-dd");
                java.util.Date temppidate = sdf.parse(temp);
                beginDate = new Date(temppidate.getTime());
                endDate = new Date(temppidate.getTime() + (24 * 3600 * 1000));
            } catch (Exception e) {
                System.out.println("Ollos virhe saapunut, siks ohjelmamme kaatunut! --Vanha kalevalalainen sananlasku--");
                e.printStackTrace();
            }
            pst2.setDate(1, beginDate);
            pst2.setDate(2, endDate);
            rs2 = pst2.executeQuery();
            MainFrame.appendStatusText("Poistetaan p�iv�n \"" + s.getDate() + "\" vanhat reseptit kannasta");
            while (rs2.next()) {
                pst3.clearParameters();
                pst3.setTimestamp(1, rs2.getTimestamp("time"));
                pst3.setInt(2, rs2.getInt("meal"));
                pst3.setInt(3, rs2.getInt("recipe_id"));
                pst3.executeUpdate();
            }
            if (s.getBreakfast() != null) {
                MainFrame.appendStatusText("Lis�t��n aamupala \"" + s.getBreakfast().getName() + "\"");
                pst1.clearParameters();
                pst1.setInt(1, s.getBreakfast().getId());
                pst1.setTimestamp(2, new Timestamp(s.getDate().getTime()));
                pst1.setInt(3, 1);
                pst1.executeUpdate();
            }
            if (s.getLunch() != null) {
                MainFrame.appendStatusText("Lis�t��n lounas \"" + s.getLunch().getName() + "\"");
                pst1.clearParameters();
                pst1.setInt(1, s.getLunch().getId());
                pst1.setTimestamp(2, new Timestamp(s.getDate().getTime()));
                pst1.setInt(3, 2);
                pst1.executeUpdate();
            }
            if (s.getSnack() != null) {
                MainFrame.appendStatusText("Lis�t��n v�lipala \"" + s.getSnack().getName() + "\"");
                pst1.clearParameters();
                pst1.setInt(1, s.getSnack().getId());
                pst1.setTimestamp(2, new Timestamp(s.getDate().getTime()));
                pst1.setInt(3, 3);
                pst1.executeUpdate();
            }
            if (s.getDinner() != null) {
                MainFrame.appendStatusText("Lis�t��n p�iv�llinen \"" + s.getDinner().getName() + "\"");
                pst1.clearParameters();
                pst1.setInt(1, s.getDinner().getId());
                pst1.setTimestamp(2, new Timestamp(s.getDate().getTime()));
                pst1.setInt(3, 4);
                pst1.executeUpdate();
            }
            if (s.getSupper() != null) {
                MainFrame.appendStatusText("Lis�t��n illallinen \"" + s.getSupper().getName() + "\"");
                pst1.clearParameters();
                pst1.setInt(1, s.getSupper().getId());
                pst1.setTimestamp(2, new Timestamp(s.getDate().getTime()));
                pst1.setInt(3, 5);
                pst1.executeUpdate();
            }
            conn.commit();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                MainFrame.appendStatusText("Aterioiden lis�ys ep�onnistui");
                e1.printStackTrace();
            }
            MainFrame.appendStatusText("Can't add schedule, the exception was " + e.getMessage());
        } finally {
            try {
                if (rs2 != null) rs2.close();
                rs2 = null;
                if (pst1 != null) pst1.close();
                pst1 = null;
                if (pst2 != null) pst2.close();
                pst2 = null;
            } catch (SQLException sqle) {
                MainFrame.appendStatusText("Can't close database connection.");
            }
        }
        return retVal;
    }
