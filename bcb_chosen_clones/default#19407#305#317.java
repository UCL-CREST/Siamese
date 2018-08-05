    private void formWindowClosed(java.awt.event.WindowEvent evt) {
        if (conn != null) {
            try {
                DriverManager.getConnection("jdbc:derby:;shutdown=true");
            } catch (SQLException se) {
                if (((se.getErrorCode() == 50000) && ("XJ015".equals(se.getSQLState())))) {
                    System.out.println("Derby shut down normally");
                } else {
                    System.err.println("Derby did not shut down normally");
                }
            }
        }
    }
