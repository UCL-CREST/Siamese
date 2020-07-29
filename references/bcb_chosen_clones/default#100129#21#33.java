    public static void main(String[] args) throws IllegalArgumentException, NotSupportedEventException, ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:test_db");
        IDescriptorReader reader = new JdbcReader(conn, "event_descriptors");
        DescriptorCollection coll = reader.read();
        System.out.println(coll.size());
        EventBuilder builder = new EventBuilder();
        List<IEvent> events = builder.buildEvents(coll);
        final int year = 2009;
        for (IEvent event : events) {
            System.out.println(event.getDate(year));
        }
    }
