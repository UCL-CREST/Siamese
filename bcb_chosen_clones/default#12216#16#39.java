    public static void main(final String[] args) throws Exception {
        final DataSource dataSource = new DatabaseConnectionOptions("org.hsqldb.jdbcDriver", "jdbc:hsqldb:hsql://localhost:9001/schemacrawler");
        final Connection connection = dataSource.getConnection("sa", "");
        final SchemaCrawlerOptions options = new SchemaCrawlerOptions();
        options.setSchemaInfoLevel(SchemaInfoLevel.standard());
        options.setProcedureInclusionRule(new InclusionRule(InclusionRule.NONE, InclusionRule.ALL));
        options.setSchemaInclusionRule(new InclusionRule("PUBLIC.BOOKS", InclusionRule.NONE));
        options.setAlphabeticalSortForTableColumns(true);
        final Database database = SchemaCrawlerUtility.getDatabase(connection, options);
        for (final Schema schema : database.getSchemas()) {
            System.out.println(schema);
            for (final Table table : schema.getTables()) {
                System.out.print("o--> " + table);
                if (table instanceof View) {
                    System.out.println(" (VIEW)");
                } else {
                    System.out.println();
                }
                for (final Column column : table.getColumns()) {
                    System.out.println("     o--> " + column + " (" + column.getType() + ")");
                }
            }
        }
    }
