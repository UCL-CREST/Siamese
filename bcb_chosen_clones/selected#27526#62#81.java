    public void generate(String rootDir, RootModel root) throws Exception {
        IOUtils.copyStream(HTMLGenerator.class.getResourceAsStream("stylesheet.css"), new FileOutputStream(new File(rootDir, "stylesheet.css")));
        Velocity.init();
        VelocityContext context = new VelocityContext();
        context.put("model", root);
        context.put("util", new VelocityUtils());
        context.put("msg", messages);
        processTemplate("index.html", new File(rootDir, "index.html"), context);
        processTemplate("list.html", new File(rootDir, "list.html"), context);
        processTemplate("summary.html", new File(rootDir, "summary.html"), context);
        File imageDir = new File(rootDir, "images");
        imageDir.mkdir();
        IOUtils.copyStream(HTMLGenerator.class.getResourceAsStream("primarykey.gif"), new FileOutputStream(new File(imageDir, "primarykey.gif")));
        File tableDir = new File(rootDir, "tables");
        tableDir.mkdir();
        for (TableModel table : root.getTables()) {
            context.put("table", table);
            processTemplate("table.html", new File(tableDir, table.getTableName() + ".html"), context);
        }
    }
