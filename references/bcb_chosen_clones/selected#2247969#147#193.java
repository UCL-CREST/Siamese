    public int addRecipe(Recipe recipe) throws Exception {
        PreparedStatement pst1 = null;
        PreparedStatement pst2 = null;
        ResultSet rs = null;
        int retVal = -1;
        try {
            conn = getConnection();
            pst1 = conn.prepareStatement("INSERT INTO recipes (name, instructions, category_id) VALUES (?, ?, ?)");
            pst1.setString(1, recipe.getName());
            pst1.setString(2, recipe.getInstructions());
            pst1.setInt(3, recipe.getCategoryId());
            if (pst1.executeUpdate() > 0) {
                pst2 = conn.prepareStatement("SELECT recipe_id FROM recipes WHERE name = ? AND instructions = ? AND category_id = ?");
                pst2.setString(1, recipe.getName());
                pst2.setString(2, recipe.getInstructions());
                pst2.setInt(3, recipe.getCategoryId());
                rs = pst2.executeQuery();
                conn.commit();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    addIngredients(recipe, id);
                    MainFrame.recipePanel.update();
                    retVal = id;
                } else {
                    retVal = -1;
                }
            } else {
                retVal = -1;
            }
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            MainFrame.appendStatusText("Can't add recipe, the exception was " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                rs = null;
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
