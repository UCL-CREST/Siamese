    public void genCreateSchema(DiagramModel diagramModel, String source) {
        try {
            con.setAutoCommit(false);
            stmt = con.createStatement();
            Collection boxes = diagramModel.getBoxes();
            BoxModel box;
            ItemModel item;
            String sqlQuery;
            int counter = 0;
            for (Iterator x = boxes.iterator(); x.hasNext(); ) {
                box = (BoxModel) x.next();
                if (!box.isAbstractDef()) {
                    sqlQuery = sqlCreateTableBegin(box);
                    Collection items = box.getItems();
                    for (Iterator y = items.iterator(); y.hasNext(); ) {
                        item = (ItemModel) y.next();
                        sqlQuery = sqlQuery + sqlColumn(item);
                    }
                    sqlQuery = sqlQuery + sqlForeignKeyColumns(box);
                    sqlQuery = sqlQuery + sqlPrimaryKey(box);
                    sqlQuery = sqlQuery + sqlUniqueKey(box);
                    sqlQuery = sqlQuery + sqlCreateTableEnd(box, source);
                    System.out.println(sqlQuery);
                    try {
                        stmt.executeUpdate(sqlQuery);
                        counter++;
                    } catch (SQLException e) {
                        String tableName = box.getName();
                        System.out.println("// Problem while creating table " + tableName + " : " + e.getMessage());
                        String msg = Para.getPara().getText("tableNotCreated") + " -- " + tableName;
                        this.informUser(msg);
                    }
                }
            }
            this.genCreateForeignKeys(diagramModel);
            con.commit();
            if (counter > 0) {
                String msg = Para.getPara().getText("schemaCreated") + " -- " + counter + " " + Para.getPara().getText("tables");
                this.informUser(msg);
            } else {
                this.informUser(Para.getPara().getText("schemaNotCreated"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage() + " // Problem with the JDBC schema generation! ");
            try {
                con.rollback();
                this.informUser(Para.getPara().getText("schemaNotCreated"));
            } catch (SQLException e1) {
                System.out.println(e1.getMessage() + " // Problem with the connection rollback! ");
            }
        } finally {
            try {
                con.setAutoCommit(true);
                stmt.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage() + " // Problem with the statement closing! ");
            }
        }
    }
