    public boolean setRecipeToTimetable(int recipeId, Timestamp time, int meal) {
        System.out.println("setRecipeToTimetable");
        PreparedStatement statement = null;
        StringBuffer query = new StringBuffer("insert into timetable (recipe_id, time, meal) values (?,?,?)");
        try {
            conn = getConnection();
            statement = conn.prepareStatement(query.toString());
            statement.setInt(1, recipeId);
            statement.setTimestamp(2, time);
            statement.setInt(3, meal);
            statement.executeUpdate();
            conn.commit();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {
            }
            MainFrame.appendStatusText("Error when trying to execute sql: " + e.getMessage());
        } finally {
            try {
                if (statement != null) statement.close();
                statement = null;
            } catch (Exception ex) {
                MainFrame.appendStatusText("Can't close database connection.");
            }
        }
        return true;
    }
