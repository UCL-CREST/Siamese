    public boolean setCliente(int IDcliente, String nombre, String paterno, String materno, String ocupacion, String rfc) {
        boolean inserto = false;
        try {
            stm = conexion.prepareStatement("insert into clientes values( '" + IDcliente + "' , '" + nombre.toUpperCase() + "' , '" + paterno.toUpperCase() + "' , '" + materno.toUpperCase() + "' , '" + ocupacion.toUpperCase() + "' , '" + rfc + "' )");
            stm.executeUpdate();
            conexion.commit();
            inserto = true;
        } catch (SQLException e) {
            System.out.println("error al insertar registro en la tabla clientes general  " + e.getMessage());
            try {
                conexion.rollback();
            } catch (SQLException ee) {
                System.out.println(ee.getMessage());
            }
            return inserto = false;
        }
        return inserto;
    }
