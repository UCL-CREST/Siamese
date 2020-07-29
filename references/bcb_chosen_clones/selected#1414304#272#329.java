    @Override
    public boolean updateProductIfAvailable(Map<String, Integer> carro, HttpServletRequest request, Map<Producto, Integer> listado) {
        Connection conexion = null;
        PreparedStatement select = null;
        PreparedStatement update = null;
        ResultSet rs = null;
        boolean exito = false;
        try {
            conexion = pool.getConnection();
            conexion.setAutoCommit(false);
            select = conexion.prepareStatement("SELECT* FROM " + nameBD + ".Productos WHERE Codigo=?");
            update = conexion.prepareStatement("UPDATE " + nameBD + ".Productos SET Stock=? WHERE Codigo=?");
            String codigoProd;
            int filasAfectadas = 0;
            Iterator<String> iterador = carro.keySet().iterator();
            while (iterador.hasNext()) {
                codigoProd = iterador.next();
                select.setString(1, codigoProd);
                rs = select.executeQuery();
                if (rs.next() == false) {
                    Tools.anadirMensaje(request, "No existe el producto con codigo: " + codigoProd + "(producto eliminado de la cesta)");
                    iterador.remove();
                    conexion.rollback();
                } else {
                    Producto prod = new Producto(rs.getString("Codigo"), rs.getString("Nombre"), rs.getDouble("Precio"), rs.getInt("Stock"), rs.getString("Descripcion"), rs.getString("Detalles"));
                    select.clearParameters();
                    if (carro.get(codigoProd) > prod.getStock()) {
                        Tools.anadirMensaje(request, "No hay unidades suficientes de: " + prod.getNombre() + "(producto eliminado de la cesta)");
                        iterador.remove();
                        conexion.rollback();
                    } else {
                        update.setInt(1, prod.getStock() - carro.get(codigoProd));
                        update.setString(2, codigoProd);
                        filasAfectadas = update.executeUpdate();
                        if (filasAfectadas != 1) {
                            Tools.anadirMensaje(request, "Ocurrio un error en el catalogo");
                            conexion.rollback();
                        }
                        update.clearParameters();
                        listado.put(prod, carro.get(codigoProd));
                    }
                }
            }
            conexion.commit();
            exito = true;
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error actualizando unidades de productos en compra", ex);
            try {
                conexion.rollback();
            } catch (SQLException ex1) {
                logger.log(Level.SEVERE, "Error haciendo rolback de la transacción que ha dado error en la actualización de unidades por compra", ex1);
            }
        } finally {
            cerrarConexionYStatement(conexion, select, update);
            cerrarResultSet(rs);
        }
        return exito;
    }
