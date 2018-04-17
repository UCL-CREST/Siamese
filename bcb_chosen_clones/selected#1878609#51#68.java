    public boolean setDeleteCliente(int IDcliente) {
        boolean delete = false;
        try {
            stm = conexion.prepareStatement("delete clientes where IDcliente='" + IDcliente + "'");
            stm.executeUpdate();
            conexion.commit();
            delete = true;
        } catch (SQLException e) {
            System.out.println("Error en la eliminacion del registro en tabla clientes " + e.getMessage());
            try {
                conexion.rollback();
            } catch (SQLException ee) {
                System.out.println(ee.getMessage());
            }
            return delete = false;
        }
        return delete;
    }
