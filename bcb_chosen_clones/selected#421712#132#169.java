    private void addDocToDB(String action, DataSource database) {
        String typeOfDoc = findTypeOfDoc(action).trim().toLowerCase();
        Connection con = null;
        try {
            con = database.getConnection();
            con.setAutoCommit(false);
            checkDupDoc(typeOfDoc, con);
            String add = "insert into " + typeOfDoc + " values( ?, ?, ?, ?, ?, ?, ? )";
            PreparedStatement prepStatement = con.prepareStatement(add);
            prepStatement.setString(1, selectedCourse.getCourseId());
            prepStatement.setString(2, selectedCourse.getAdmin());
            prepStatement.setTimestamp(3, getTimeStamp());
            prepStatement.setString(4, getLink());
            prepStatement.setString(5, homePage.getUser());
            prepStatement.setString(6, getText());
            prepStatement.setString(7, getTitle());
            prepStatement.executeUpdate();
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
    }
