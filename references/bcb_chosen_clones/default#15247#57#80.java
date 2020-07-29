    public void elimina(Pedido pe) throws errorSQL, errorConexionBD {
        System.out.println("GestorPedido.elimina()");
        int id = pe.getId();
        String sql;
        Statement stmt = null;
        try {
            gd.begin();
            sql = "DELETE FROM pedido WHERE id=" + id;
            System.out.println("Ejecutando: " + sql);
            stmt = gd.getConexion().createStatement();
            stmt.executeUpdate(sql);
            System.out.println("executeUpdate");
            gd.commit();
            System.out.println("commit");
            stmt.close();
        } catch (SQLException e) {
            gd.rollback();
            throw new errorSQL(e.toString());
        } catch (errorConexionBD e) {
            System.err.println("Error en GestorPedido.elimina(): " + e);
        } catch (errorSQL e) {
            System.err.println("Error en GestorPedido.elimina(): " + e);
        }
    }
