    @Test
    public void testSecondary() throws Exception {
        ConnectionFactoryIF cf = new DefaultConnectionFactory(PropertyUtils.loadProperties(StreamUtils.getInputStream(propfile)), false);
        Connection conn = cf.requestConnection();
        try {
            Statement stm = conn.createStatement();
            stm.executeUpdate("drop table if exists first");
            stm.executeUpdate("drop table if exists first_changes");
            stm.executeUpdate("drop table if exists second");
            stm.executeUpdate("drop table if exists second_changes");
            stm.executeUpdate("create table first (a integer, b varchar, c integer, d date)");
            stm.executeUpdate("create table first_changes (a integer, b varchar, c integer, d date, ct varchar, cd integer)");
            stm.executeUpdate("create table second (a integer, b varchar, c integer, d date)");
            stm.executeUpdate("create table second_changes (a integer, b varchar, c integer, d date, ct varchar, cd integer)");
            stm.executeUpdate("insert into first (a,b,c,d) values (1,'a',10, date '2007-01-01')");
            stm.executeUpdate("insert into first (a,b,c,d) values (2,'b',20, date '2007-01-02')");
            stm.executeUpdate("insert into first (a,b,c,d) values (3,'c',30, date '2007-01-03')");
            stm.executeUpdate("insert into first (a,b,c,d) values (4,'d',40, date '2007-01-04')");
            stm.executeUpdate("insert into second (a,b,c,d) values (1,'e',50, date '2007-02-01')");
            stm.executeUpdate("insert into second (a,b,c,d) values (2,'f',60, date '2007-02-02')");
            stm.executeUpdate("insert into second (a,b,c,d) values (3,'g',70, date '2007-02-03')");
            stm.executeUpdate("insert into second (a,b,c,d) values (4,'h',80, date '2007-02-04')");
            conn.commit();
            RelationMapping mapping = RelationMapping.readFromClasspath("net/ontopia/topicmaps/db2tm/JDBCDataSourceTest-secondary.xml");
            TopicMapStoreIF store = new InMemoryTopicMapStore();
            LocatorIF baseloc = URIUtils.getURILocator("base:foo");
            store.setBaseAddress(baseloc);
            TopicMapIF topicmap = store.getTopicMap();
            Processor.addRelations(mapping, null, topicmap, baseloc);
            exportTopicMap(topicmap, "after-first-sync");
            stm.executeUpdate("insert into second_changes (a,b,c,d,ct,cd) values (2,'f',60,date '2007-02-02', 'r', 2)");
            stm.executeUpdate("delete from second where a = 2");
            conn.commit();
            Processor.synchronizeRelations(mapping, null, topicmap, baseloc);
            exportTopicMap(topicmap, "after-second-sync");
            mapping.close();
            stm.executeUpdate("drop table first");
            stm.executeUpdate("drop table first_changes");
            stm.executeUpdate("drop table second");
            stm.executeUpdate("drop table second_changes");
            stm.close();
            store.close();
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.close();
        }
    }
