    public boolean conectar() {
        boolean hasErrors = false;
        try {
            conexion = DriverManager.getConnection("jdbc:mysql://localhost/mysql", "pof_billetera", "pof123456");
        } catch (java.sql.SQLException sqle) {
            javax.swing.JLabel label = new javax.swing.JLabel("Ingrese la contraseña de \"root\" para mysql: ");
            javax.swing.JPasswordField jpf = new javax.swing.JPasswordField();
            javax.swing.JOptionPane.showMessageDialog(null, new Object[] { label, jpf }, "Password:", javax.swing.JOptionPane.OK_CANCEL_OPTION);
            String contrasena = jpf.getText();
            try {
                conexion = DriverManager.getConnection("jdbc:mysql://localhost/mysql", "root", contrasena);
                Statement instruccionesBD = conexion.createStatement();
                instruccionesBD.execute("GRANT ALL ON *.* TO 'pof_billetera' IDENTIFIED BY 'pof123456'");
            } catch (java.sql.SQLException sqle2) {
                javax.swing.JOptionPane.showMessageDialog(null, "Acceso a la base de datos denegado. Revise que haya colocado su usuario como root y que haya ingresado la contraseña correcta a la base de datos.");
                hasErrors = true;
            }
        } catch (Exception noSQL) {
            javax.swing.JOptionPane.showMessageDialog(null, "Para poder utilizar este programa, debe de tener instalado la base de datos MySQL, además de configurarlo en el localhost.");
            hasErrors = true;
        }
        return !hasErrors;
    }
