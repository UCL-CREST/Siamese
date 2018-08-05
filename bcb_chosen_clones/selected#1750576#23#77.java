    public void executeQuery(Connection connection, String query) {
        action = null;
        updateCount = 0;
        resultsAvailable = false;
        metaAvailable = false;
        planAvailable = false;
        if (connection == null) {
            ide.setStatus("not connected");
            return;
        }
        cleanUp();
        try {
            ide.setStatus("Executing query");
            stmt = connection.createStatement();
            if (query.toLowerCase().startsWith("select")) {
                result = stmt.executeQuery(query);
                resultsAvailable = true;
                action = "select";
            } else if (query.toLowerCase().startsWith("update")) {
                updateCount = stmt.executeUpdate(query);
                action = "update";
            } else if (query.toLowerCase().startsWith("delete")) {
                updateCount = stmt.executeUpdate(query);
                action = "delete";
            } else if (query.toLowerCase().startsWith("insert")) {
                updateCount = stmt.executeUpdate(query);
                action = "insert";
            } else if (query.toLowerCase().startsWith("commit")) {
                connection.commit();
                action = "commit";
            } else if (query.toLowerCase().startsWith("rollback")) {
                connection.rollback();
                action = "rollback";
            } else if (query.toLowerCase().startsWith("create")) {
                updateCount = stmt.executeUpdate(query);
                action = "create";
            } else if (query.toLowerCase().startsWith("drop")) {
                updateCount = stmt.executeUpdate(query);
                action = "drop";
            } else if (query.toLowerCase().startsWith("desc ")) {
                String objectName = query.substring(query.indexOf(' '), query.length());
                query = "select * from (" + objectName + ") where rownum < 1";
                descQuery(connection, query);
            } else if (query.toLowerCase().startsWith("explain plan for ")) {
                explainQuery(connection, query);
            } else {
                result = stmt.executeQuery(query);
                resultsAvailable = true;
                action = "select";
            }
            ide.setStatus("executed query");
        } catch (Exception e) {
            ide.setStatus(e.getMessage());
        }
    }
