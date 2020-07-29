    public boolean setUpdateCliente(int IDcliente, String nombre, String paterno, String materno, String ocupacion, String rfc) {
        boolean update = false;
        try {
            stm = conexion.prepareStatement("update clientes set nombre='" + nombre.toUpperCase().trim() + "' , paterno='" + paterno.toUpperCase().trim() + "' ," + "materno='" + materno.toUpperCase().trim() + "',ocupacion='" + ocupacion.toUpperCase().trim() + "',rfc='" + rfc.trim() + "' where IDcliente ='" + IDcliente + "' ");
            stm.executeUpdate();
            conexion.commit();
            update = true;
        } catch (SQLException e) {
            System.out.println("error al actualizar registro en la tabla clientes  " + e.getMessage());
            try {
                conexion.rollback();
            } catch (SQLException ee) {
                System.out.println(ee.getMessage());
            }
            return update = false;
        }
        return update;
    }
