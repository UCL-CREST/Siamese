    private void updateIngredients(Recipe recipe, int id) throws Exception {
        PreparedStatement pst1 = null;
        PreparedStatement pst2 = null;
        try {
            conn = getConnection();
            pst1 = conn.prepareStatement("DELETE FROM ingredients WHERE recipe_id = ?");
            pst1.setInt(1, id);
            if (pst1.executeUpdate() >= 0) {
                pst2 = conn.prepareStatement("INSERT INTO ingredients (recipe_id, name, amount, measure_id, shop_flag) VALUES (?,?,?,?,?)");
                IngredientContainer ings = recipe.getIngredients();
                Ingredient ingBean = null;
                Iterator it;
                for (it = ings.getIngredients().iterator(); it.hasNext(); ) {
                    ingBean = (Ingredient) it.next();
                    pst2.setInt(1, id);
                    pst2.setString(2, ingBean.getName());
                    pst2.setDouble(3, ingBean.getAmount());
                    pst2.setInt(4, ingBean.getType());
                    pst2.setInt(5, ingBean.getShopFlag());
                    pst2.executeUpdate();
                }
            }
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            MainFrame.appendStatusText("Can't add ingredient, the exception was " + e.getMessage());
        } finally {
            try {
                if (pst1 != null) pst1.close();
                pst1 = null;
                if (pst2 != null) pst2.close();
                pst2 = null;
            } catch (Exception ex) {
                MainFrame.appendStatusText("Can't close database connection.");
            }
        }
    }
