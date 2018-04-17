    @Override
    public boolean saveCart(Carrito cart, boolean completado, String date, String formPago) {
        Connection conexion = null;
        PreparedStatement insertHistorial = null;
        PreparedStatement insertCarrito = null;
        boolean exito = false;
        try {
            conexion = pool.getConnection();
            conexion.setAutoCommit(false);
            insertHistorial = conexion.prepareStatement("INSERT INTO " + nameBD + ".HistorialCarritos VALUES (?,?,?,?,?,?)");
            insertHistorial.setString(1, cart.getUser());
            insertHistorial.setString(2, cart.getCodigo());
            insertHistorial.setString(3, date);
            insertHistorial.setDouble(4, cart.getPrecio());
            insertHistorial.setString(5, formPago);
            insertHistorial.setBoolean(6, completado);
            int filasAfectadas = insertHistorial.executeUpdate();
            if (filasAfectadas != 1) {
                conexion.rollback();
            } else {
                insertCarrito = conexion.prepareStatement("INSERT INTO " + nameBD + ".Carritos VALUES (?,?,?,?,?)");
                Iterator<String> iteradorProductos = cart.getArticulos().keySet().iterator();
                while (iteradorProductos.hasNext()) {
                    String key = iteradorProductos.next();
                    Producto prod = getProduct(key);
                    int cantidad = cart.getArticulos().get(key);
                    insertCarrito.setString(1, cart.getCodigo());
                    insertCarrito.setString(2, prod.getCodigo());
                    insertCarrito.setString(3, prod.getNombre());
                    insertCarrito.setDouble(4, prod.getPrecio());
                    insertCarrito.setInt(5, cantidad);
                    filasAfectadas = insertCarrito.executeUpdate();
                    if (filasAfectadas != 1) {
                        conexion.rollback();
                        break;
                    }
                    insertCarrito.clearParameters();
                }
                conexion.commit();
                exito = true;
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error añadiendo carrito al registro", ex);
            try {
                conexion.rollback();
            } catch (SQLException ex1) {
                logger.log(Level.SEVERE, "Error haciendo rollback de la transacción para insertar carrito en el registro", ex1);
            }
        } finally {
            cerrarConexionYStatement(conexion, insertCarrito, insertHistorial);
        }
        return exito;
    }
