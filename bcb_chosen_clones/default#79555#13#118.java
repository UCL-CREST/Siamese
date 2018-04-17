    public boolean login() throws IOException {
        String passwd;
        boolean issamepass = false;
        boolean usernameexists = true;
        boolean validlogin = false;
        Context initCtx = null, envCtx = null;
        DataSource ds = null;
        Connection con = null;
        try {
            initCtx = new InitialContext();
            envCtx = (Context) initCtx.lookup("tbbs:comp/env");
            ds = (DataSource) envCtx.lookup("jdbc/tbbsDB");
            con = ds.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        File passfile = null;
        FileOutputStream outpass;
        FileInputStream inpass;
        java.sql.Statement stmt = null;
        ResultSet rset = null;
        io.textColor(io.fg_brgreen);
        io.println("Type NEW to create a new user.");
        while (!validlogin) {
            io.textColor(io.fg_bryellow + io.bg_black);
            io.print("login: ");
            io.textColor(io.fg_cyan);
            ud.userName = io.inputLine(32, false);
            if (ud.userName.equalsIgnoreCase("new")) {
                while (usernameexists) {
                    io.textColor(io.bg_black);
                    io.textColor(io.fg_brblue);
                    io.println("Please input your new username:");
                    io.textColor(io.fg_green);
                    ud.userName = io.inputLine(32, false);
                    if (server.checkUserExists(ud.userName)) {
                        io.textColor(io.fg_brmagenta);
                        io.println("That username is taken, please select another");
                    } else usernameexists = false;
                }
                while (!issamepass) {
                    io.textColor(io.bg_black);
                    io.textColor(io.fg_brblue);
                    io.println("Please input your password:");
                    io.textColor(io.fg_green);
                    passwd = io.inputLine(32, true);
                    io.textColor(io.fg_brblue);
                    io.println("Please reinput your password:");
                    io.textColor(io.fg_green);
                    if (passwd.equals(io.inputLine(32, true))) {
                        io.textColor(io.bg_red);
                        io.textColor(io.fg_white);
                        io.println("New user created.  Welcome to the Telnet BBS!");
                        io.textColor(io.bg_black);
                        io.println("Please change your optional user settings when possible.\n\n");
                        try {
                            stmt = con.createStatement();
                            rset = stmt.executeQuery("INSERT INTO user_tbl VALUES (" + '"' + ud.userName + '"' + ", " + '"' + passwd + '"' + ", NULL, NULL, NULL, NULL);");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        issamepass = true;
                        validlogin = true;
                        io.textColor(io.bg_black);
                    } else {
                        io.textColor(io.fg_brmagenta);
                        io.println("The passwords did not match, please retype them");
                    }
                }
            } else {
                io.textColor(io.fg_bryellow);
                io.print("password: ");
                io.textColor(io.fg_cyan);
                passwd = io.inputLine(32, true);
                try {
                    stmt = con.createStatement();
                    rset = stmt.executeQuery("SELECT password FROM user_tbl WHERE userid = " + '"' + ud.userName + '"' + ";");
                    if (rset.next()) {
                        String pass = rset.getString(1);
                        if (!passwd.equals(pass.trim())) {
                            io.textColor(io.fg_brmagenta);
                            io.println("Incorrect password or username");
                        } else validlogin = true;
                    } else {
                        io.textColor(io.fg_brmagenta);
                        io.println("Incorrect password or username");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        io.textColor(io.fg_brred);
        io.println("Welcome! You are connected on node " + ud.node + ".");
        io.log("User \"" + ud.userName + "\" logged in.");
        io.textColor(io.fg_gray);
        io.pressAnyKey();
        io.clearScreen();
        try {
            con.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
