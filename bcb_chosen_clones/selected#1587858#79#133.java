    private int addPollToDB(DataSource database) {
        int pollid = -2;
        Connection con = null;
        try {
            con = database.getConnection();
            con.setAutoCommit(false);
            String add = "insert into polls" + " values( ?, ?, ?, ?)";
            PreparedStatement prepStatement = con.prepareStatement(add);
            prepStatement.setString(1, selectedCourse.getAdmin());
            prepStatement.setString(2, selectedCourse.getCourseId());
            prepStatement.setString(3, getTitle());
            prepStatement.setInt(4, 0);
            prepStatement.executeUpdate();
            String findNewID = "select max(pollid) from polls";
            prepStatement = con.prepareStatement(findNewID);
            ResultSet newID = prepStatement.executeQuery();
            pollid = -2;
            while (newID.next()) {
                pollid = newID.getInt(1);
            }
            if (pollid == -2) {
                this.sqlError = true;
                throw new Exception();
            }
            String[] options = getAllOptions();
            for (int i = 0; i < 4; i++) {
                String insertOption = "insert into polloptions values ( ?, ?, ? )";
                prepStatement = con.prepareStatement(insertOption);
                prepStatement.setString(1, options[i]);
                prepStatement.setInt(2, 0);
                prepStatement.setInt(3, pollid);
                prepStatement.executeUpdate();
            }
            prepStatement.close();
            con.commit();
        } catch (Exception e) {
            sqlError = true;
            e.printStackTrace();
            if (con != null) try {
                con.rollback();
            } catch (Exception logOrIgnore) {
            }
            try {
                throw e;
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } finally {
            if (con != null) try {
                con.close();
            } catch (Exception logOrIgnore) {
            }
        }
        return pollid;
    }
