    public static void main(String[] args) {
        String url = "jdbc:mySubprotocol:myDataSource";
        Connection con;
        Statement stmt;
        try {
            Class.forName("myDriver.ClassName");
        } catch (java.lang.ClassNotFoundException e) {
            System.err.print("ClassNotFoundException: ");
            System.err.println(e.getMessage());
        }
        try {
            con = DriverManager.getConnection(url, "myLogin", "myPassword");
            stmt = con.createStatement();
            Vector dataTypes = getDataTypes(con);
            String tableName;
            String columnName;
            String sqlType;
            String prompt = "Enter the new table name and hit Return: ";
            tableName = getInput(prompt);
            String createTableString = "create table " + tableName + " (";
            String commaAndSpace = ", ";
            boolean firstTime = true;
            while (true) {
                System.out.println("");
                prompt = "Enter a column name " + "(or nothing when finished) \nand hit Return: ";
                columnName = getInput(prompt);
                if (firstTime) {
                    if (columnName.length() == 0) {
                        System.out.print("Need at least one column;");
                        System.out.println(" please try again");
                        continue;
                    } else {
                        createTableString += columnName + " ";
                        firstTime = false;
                    }
                } else if (columnName.length() == 0) {
                    break;
                } else {
                    createTableString += commaAndSpace + columnName + " ";
                }
                String localTypeName = null;
                String paramString = "";
                while (true) {
                    System.out.println("");
                    System.out.println("LIST OF TYPES YOU MAY USE:  ");
                    boolean firstPrinted = true;
                    int length = 0;
                    for (int i = 0; i < dataTypes.size(); i++) {
                        DataType dataType = (DataType) dataTypes.get(i);
                        if (!dataType.needsToBeSet()) {
                            if (!firstPrinted) System.out.print(commaAndSpace); else firstPrinted = false;
                            System.out.print(dataType.getSQLType());
                            length += dataType.getSQLType().length();
                            if (length > 50) {
                                System.out.println("");
                                length = 0;
                                firstPrinted = true;
                            }
                        }
                    }
                    System.out.println("");
                    int index;
                    prompt = "Enter a column type " + "from the list and hit Return:  ";
                    sqlType = getInput(prompt);
                    for (index = 0; index < dataTypes.size(); index++) {
                        DataType dataType = (DataType) dataTypes.get(index);
                        if (dataType.getSQLType().equalsIgnoreCase(sqlType) && !dataType.needsToBeSet()) {
                            break;
                        }
                    }
                    localTypeName = null;
                    paramString = "";
                    if (index < dataTypes.size()) {
                        String params;
                        DataType dataType = (DataType) dataTypes.get(index);
                        params = dataType.getParams();
                        localTypeName = dataType.getLocalType();
                        if (params != null) {
                            prompt = "Enter " + params + ":  ";
                            paramString = "(" + getInput(prompt) + ")";
                        }
                        break;
                    } else {
                        prompt = "Are you sure?  " + "Enter 'y' or 'n' and hit Return:  ";
                        String check = getInput(prompt) + " ";
                        check = check.toLowerCase().substring(0, 1);
                        if (check.equals("n")) continue; else {
                            localTypeName = sqlType;
                            break;
                        }
                    }
                }
                createTableString += localTypeName + paramString;
            }
            createTableString += ")";
            System.out.println("");
            System.out.print("Your CREATE TABLE statement as ");
            System.out.println("sent to your DBMS:  ");
            System.out.println(createTableString);
            System.out.println("");
            stmt.executeUpdate(createTableString);
            stmt.close();
            con.close();
        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }
    }
