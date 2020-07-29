    public void elimina(Cliente cli) throws errorSQL, errorConexionBD {
        System.out.println("GestorCliente.elimina()");
        int id = cli.getId();
        String sql;
        Statement stmt = null;
        try {
            gd.begin();
            sql = "DELETE FROM cliente WHERE cod_cliente =" + id;
            System.out.println("Ejecutando: " + sql);
            stmt = gd.getConexion().createStatement();
            stmt.executeUpdate(sql);
            System.out.println("executeUpdate");
            sql = "DELETE FROM persona WHERE id =" + id;
            System.out.println("Ejecutando: " + sql);
            stmt.executeUpdate(sql);
            gd.commit();
            System.out.println("commit");
            stmt.close();
        } catch (SQLException e) {
            gd.rollback();
            throw new errorSQL(e.toString());
        } catch (errorConexionBD e) {
            System.err.println("Error en GestorCliente.elimina(): " + e);
        } catch (errorSQL e) {
            System.err.println("Error en GestorCliente.elimina(): " + e);
        }
    }
