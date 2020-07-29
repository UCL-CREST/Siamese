    private void addIngredients(Recipe recipe, int id) throws Exception {
        PreparedStatement pst = null;
        try {
            conn = getConnection();
            pst = conn.prepareStatement("INSERT INTO ingredients (recipe_id, name, amount, measure_id, shop_flag) VALUES (?,?,?,?,?)");
            IngredientContainer ings = recipe.getIngredients();
            Ingredient ingBean = null;
            Iterator it;
            for (it = ings.getIngredients().iterator(); it.hasNext(); ) {
                ingBean = (Ingredient) it.next();
                pst.setInt(1, id);
                pst.setString(2, ingBean.getName());
                pst.setDouble(3, ingBean.getAmount());
                pst.setInt(4, ingBean.getType());
                pst.setInt(5, ingBean.getShopFlag());
                pst.executeUpdate();
            }
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            MainFrame.appendStatusText("Can't add ingredient, the exception was " + e.getMessage());
        } finally {
            try {
                if (pst != null) pst.close();
                pst = null;
            } catch (Exception ex) {
                MainFrame.appendStatusText("Can't close database connection.");
            }
        }
    }
