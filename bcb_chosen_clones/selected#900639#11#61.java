    private void load() throws SQLException {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = FidoDataSource.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            ClearData.clearTables(stmt);
            stmt.executeUpdate("insert into AttributeCategories (CategoryName) values ('color')");
            stmt.executeUpdate("insert into Attributes (AttributeName, Category) values ('blue', 'color')");
            stmt.executeUpdate("insert into Attributes (AttributeName, Category) values ('red', 'color')");
            stmt.executeUpdate("insert into Objects (ObjectId, Description) values (100, 'ball')");
            stmt.executeUpdate("insert into Objects (ObjectId, Description) values (101, 'blue ball')");
            stmt.executeUpdate("insert into Objects (ObjectId, Description) values (102, 'red ball')");
            stmt.executeQuery("select setval('objects_objectid_seq', 1000)");
            stmt.executeUpdate("insert into ObjectLinks (ObjectId, LinkName, LinkToObject) values (100, 'isa', 1)");
            stmt.executeUpdate("insert into ObjectLinks (ObjectId, LinkName, LinkToObject) values (101, 'instance', 100)");
            stmt.executeUpdate("insert into ObjectLinks (ObjectId, LinkName, LinkToObject) values (102, 'instance', 100)");
            stmt.executeUpdate("insert into ObjectAttributes (ObjectId, AttributeName) values (101, 'blue')");
            stmt.executeUpdate("insert into ObjectAttributes (ObjectId, AttributeName) values (102, 'red')");
            stmt.executeUpdate("insert into Dictionary (Word, SenseNumber, GrammarString, ObjectId) values ('LEFT-WALL', '1', 'AV+ | ADJ+', 1)");
            stmt.executeUpdate("insert into Dictionary (Word, SenseNumber, GrammarString, ObjectId) values ('the', '1', 'D+', 1)");
            stmt.executeUpdate("insert into Dictionary (Word, SenseNumber, GrammarString, ObjectId) values ('red', '1', 'ADJ- | ADJ+', 1)");
            stmt.executeUpdate("insert into Dictionary (Word, SenseNumber, GrammarString, ObjectId) values ('blue', '1', 'ADJ- | ADJ+', 1)");
            stmt.executeUpdate("insert into Dictionary (Word, SenseNumber, GrammarString, ObjectId) values ('ball', '1', '[@ADJ-] & [D-] & DO-', 100)");
            stmt.executeUpdate("insert into Dictionary (Word, SenseNumber, GrammarString, ObjectId) values ('throw', '1', 'AV- & DO+', 1)");
            stmt.executeUpdate("insert into GrammarLinks (LinkName, LinkType) values ('DO', 3)");
            stmt.executeUpdate("insert into GrammarLinks (LinkName, LinkType) values ('AV', 7)");
            stmt.executeUpdate("insert into GrammarLinks (LinkName, LinkType) values ('D', 10)");
            stmt.executeUpdate("insert into GrammarLinks (LinkName, LinkType) values ('ADJ', 11)");
            stmt.executeUpdate("insert into Articles (ArticleName, Dereference) values ('the', 1)");
            stmt.executeUpdate("insert into FrameSlots (SlotName) values ('object')");
            stmt.executeUpdate("insert into QuestionWords (QuestionWord, Type) values ('which', 7)");
            stmt.executeUpdate("insert into Verbs (VerbName, Type, SubjectSlot, IndirectObjectSlot, PredicateNounSlot) values ('throw', 1, '', '', 'object')");
            stmt.executeQuery("select setval('instructions_instructionid_seq', 1)");
            stmt.executeUpdate("insert into Instructions (Type, ExecuteString, FrameSlot, Operator, LinkName, ObjectId, AttributeName) " + "values (3, '', null, 0, null, null, null)");
            stmt.executeQuery("select setval('transactions_transactionid_seq', 1)");
            stmt.executeUpdate("insert into Transactions (InstructionId, Description) values (2, 'throw')");
            stmt.executeQuery("select setval('verbtransactions_verbid_seq', 1)");
            stmt.executeUpdate("insert into VerbTransactions (VerbString, MoodType, TransactionId) values ('throw', 1, 2)");
            stmt.executeUpdate("insert into VerbConstraints (VerbId, FrameSlot, ObjectId) values (2, 'object', 1)");
            stmt.executeUpdate("update SystemProperties set value = 'Tutorial 3 Data' where name = 'DB Data Version'");
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }
