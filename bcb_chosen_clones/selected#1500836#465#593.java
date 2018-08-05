    @Override
    public boolean delete(String consulta, boolean autocommit, int transactionIsolation, Connection cx) throws SQLException {
        filasDelete = 0;
        if (!consulta.contains(";")) {
            this.tipoConsulta = new Scanner(consulta);
            if (this.tipoConsulta.hasNext()) {
                execConsulta = this.tipoConsulta.next();
                if (execConsulta.equalsIgnoreCase("delete")) {
                    Connection conexion = cx;
                    Statement st = null;
                    try {
                        conexion.setAutoCommit(autocommit);
                        if (transactionIsolation == 1 || transactionIsolation == 2 || transactionIsolation == 4 || transactionIsolation == 8) {
                            conexion.setTransactionIsolation(transactionIsolation);
                        } else {
                            throw new IllegalArgumentException("Valor invalido sobre TransactionIsolation,\n TRANSACTION_NONE no es soportado por MySQL");
                        }
                        st = (Statement) conexion.createStatement(ResultSetImpl.TYPE_SCROLL_SENSITIVE, ResultSetImpl.CONCUR_UPDATABLE);
                        conexion.setReadOnly(false);
                        filasDelete = st.executeUpdate(consulta.trim(), Statement.RETURN_GENERATED_KEYS);
                        if (filasDelete > -1) {
                            if (autocommit == false) {
                                conexion.commit();
                            }
                            return true;
                        } else {
                            return false;
                        }
                    } catch (MySQLIntegrityConstraintViolationException e) {
                        if (autocommit == false) {
                            try {
                                conexion.rollback();
                                System.out.println("Se ejecuto un Rollback");
                            } catch (MySQLTransactionRollbackException sqlE) {
                                System.out.println("No se ejecuto un Rollback");
                                sqlE.printStackTrace();
                            } catch (SQLException se) {
                                se.printStackTrace();
                            }
                        }
                        e.printStackTrace();
                        return false;
                    } catch (MySQLNonTransientConnectionException e) {
                        if (autocommit == false) {
                            try {
                                conexion.rollback();
                                System.out.println("Se ejecuto un Rollback");
                            } catch (MySQLTransactionRollbackException sqlE) {
                                System.out.println("No se ejecuto un Rollback");
                                sqlE.printStackTrace();
                            } catch (SQLException se) {
                                se.printStackTrace();
                            }
                        }
                        e.printStackTrace();
                        return false;
                    } catch (MySQLDataException e) {
                        System.out.println("Datos incorrectos");
                        if (autocommit == false) {
                            try {
                                conexion.rollback();
                                System.out.println("Se ejecuto un Rollback");
                            } catch (MySQLTransactionRollbackException sqlE) {
                                System.out.println("No se ejecuto un Rollback");
                                sqlE.printStackTrace();
                            } catch (SQLException se) {
                                se.printStackTrace();
                            }
                        }
                        return false;
                    } catch (MySQLSyntaxErrorException e) {
                        System.out.println("Error en la sintaxis de la Consulta en MySQL");
                        if (autocommit == false) {
                            try {
                                conexion.rollback();
                                System.out.println("Se ejecuto un Rollback");
                            } catch (MySQLTransactionRollbackException sqlE) {
                                System.out.println("No se ejecuto un Rollback");
                                sqlE.printStackTrace();
                            } catch (SQLException se) {
                                se.printStackTrace();
                            }
                        }
                        return false;
                    } catch (SQLException e) {
                        if (autocommit == false) {
                            try {
                                conexion.rollback();
                                System.out.println("Se ejecuto un Rollback");
                            } catch (MySQLTransactionRollbackException sqlE) {
                                System.out.println("No se ejecuto un Rollback");
                                sqlE.printStackTrace();
                            } catch (SQLException se) {
                                se.printStackTrace();
                            }
                        }
                        e.printStackTrace();
                        return false;
                    } finally {
                        try {
                            if (st != null) {
                                if (!st.isClosed()) {
                                    st.close();
                                }
                            }
                            if (!conexion.isClosed()) {
                                conexion.close();
                            }
                        } catch (NullPointerException ne) {
                            ne.printStackTrace();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    throw new IllegalArgumentException("No es una instruccion Delete");
                }
            } else {
                try {
                    throw new JMySQLException("Error Grave , notifique al departamento de Soporte Tecnico \n" + email);
                } catch (JMySQLException ex) {
                    Logger.getLogger(JMySQL.class.getName()).log(Level.SEVERE, null, ex);
                    return false;
                }
            }
        } else {
            throw new IllegalArgumentException("No estan permitidas las MultiConsultas en este metodo");
        }
    }
