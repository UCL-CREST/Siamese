    public int editRecipe(int oldRecipeId, Recipe newRecipe) throws Exception {
        PreparedStatement pst1 = null;
        PreparedStatement pst2 = null;
        ResultSet rs = null;
        int retVal = -1;
        try {
            conn = getConnection();
            pst1 = conn.prepareStatement("UPDATE recipes SET name = ?, instructions = ?, category_id =? WHERE recipe_id = ?");
            pst1.setString(1, newRecipe.getName());
            pst1.setString(2, newRecipe.getInstructions());
            pst1.setInt(3, newRecipe.getCategoryId());
            pst1.setInt(4, oldRecipeId);
            int rsVal = pst1.executeUpdate();
            conn.commit();
            if (rsVal > 0) {
                updateIngredients(newRecipe, oldRecipeId);
                MainFrame.recipePanel.update();
                retVal = oldRecipeId;
            } else {
                retVal = -1;
            }
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw new Exception("Can't edit recipe, the exception was " + e.getMessage());
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
