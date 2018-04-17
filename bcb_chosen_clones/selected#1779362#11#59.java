    private void load() throws SQLException {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = FidoDataSource.getConnection();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            ClearData.clearTables(stmt);
            stmt.executeUpdate("insert into Objects (ObjectId, Description) values (100, 'Person')");
            stmt.executeUpdate("insert into Objects (ObjectId, Description) values (101, 'john')");
            stmt.executeUpdate("insert into Objects (ObjectId, Description) values (200, 'Dog')");
            stmt.executeQuery("select setval('objects_objectid_seq', 1000)");
            stmt.executeUpdate("insert into ClassLinkTypes (LinkName, LinkType) values ('hasa', 2)");
            stmt.executeUpdate("insert into ObjectLinks (ObjectId, LinkName, LinkToObject) values (100, 'isa', 1)");
            stmt.executeUpdate("insert into ObjectLinks (ObjectId, LinkName, LinkToObject) values (101, 'instance', 100)");
            stmt.executeUpdate("insert into ObjectLinks (ObjectId, LinkName, LinkToObject) values (200, 'isa', 1)");
            stmt.executeUpdate("insert into Dictionary (Word, SenseNumber, GrammarString, ObjectId) values ('LEFT-WALL', '1', 'AV+', 1)");
            stmt.executeUpdate("insert into Dictionary (Word, SenseNumber, GrammarString, ObjectId) values ('john', '1', 'S+ | DO-', 1)");
            stmt.executeUpdate("insert into Dictionary (Word, SenseNumber, GrammarString, ObjectId) values ('a', '1', 'D+', 1)");
            stmt.executeUpdate("insert into Dictionary (Word, SenseNumber, GrammarString, ObjectId) values ('dog', '1', '[D-] & (S+ | DO-)', 200)");
            stmt.executeUpdate("insert into Dictionary (Word, SenseNumber, GrammarString, ObjectId) values ('have', '1', 'S- & AV- & DO+', 1)");
            stmt.executeUpdate("insert into LanguageMorphologies (LanguageName, MorphologyTag, Rank, Root, Surface, Used) values " + "                                 ('English', 'third singular', 1, 'have', 'has', TRUE)");
            stmt.executeUpdate("insert into GrammarLinks (LinkName, LinkType) values ('S', 1)");
            stmt.executeUpdate("insert into GrammarLinks (LinkName, LinkType) values ('DO', 3)");
            stmt.executeUpdate("insert into GrammarLinks (LinkName, LinkType) values ('AV', 7)");
            stmt.executeUpdate("insert into GrammarLinks (LinkName, LinkType) values ('D', 10)");
            stmt.executeUpdate("insert into Articles (ArticleName, Dereference) values ('a', 2)");
            stmt.executeUpdate("insert into FrameSlots (SlotName) values ('actor')");
            stmt.executeUpdate("insert into FrameSlots (SlotName) values ('object')");
            stmt.executeUpdate("insert into Verbs (VerbName, Type, SubjectSlot, IndirectObjectSlot, PredicateNounSlot) values ('have', 1, 'actor', '', 'object')");
            stmt.executeQuery("select setval('instructions_instructionid_seq', 1)");
            stmt.executeUpdate("insert into Instructions (Type, ExecuteString, FrameSlot, Operator, LinkName, ObjectId, AttributeName) " + "values (3, 'link %actor hasa %object', null, 0, null, null, null)");
            stmt.executeQuery("select setval('transactions_transactionid_seq', 1)");
            stmt.executeUpdate("insert into Transactions (InstructionId, Description) values (2, 'have - link')");
            stmt.executeQuery("select setval('verbtransactions_verbid_seq', 1)");
            stmt.executeUpdate("insert into VerbTransactions (VerbString, MoodType, TransactionId) values ('have', 1, 2)");
            stmt.executeUpdate("insert into VerbConstraints (VerbId, FrameSlot, ObjectId) values (2, 'actor', 1)");
            stmt.executeUpdate("insert into VerbConstraints (VerbId, FrameSlot, ObjectId) values (2, 'object', 1)");
            stmt.executeUpdate("insert into ProperNouns (Noun, SenseNumber, ObjectId) values ('john', 1, 101)");
            stmt.executeUpdate("update SystemProperties set value = 'Tutorial 1 Data' where name = 'DB Data Version'");
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
    }
