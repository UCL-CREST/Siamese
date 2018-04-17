    public Action(Database db, ResultSetMetaInfo rsmi, String name, String label, String template) throws SQLException {
        this.name = name;
        this.label = label;
        this.template = template;
        final Pattern pattern = Pattern.compile("\\{\\w+\\.?\\w+\\}");
        final Matcher matcher = pattern.matcher(template);
        int i = 0;
        while (matcher.find()) {
            i = i + 1;
        }
        this.openPos = new int[i];
        this.closePos = new int[i];
        this.ioc = new IOController[i];
        matcher.reset();
        i = 0;
        while (matcher.find()) {
            this.openPos[i] = matcher.start();
            this.closePos[i] = matcher.end();
            final String variable = template.substring(openPos[i] + 1, closePos[i] - 1);
            final String[] s = variable.split("\\.\\s*", 2);
            if (s.length > 1) {
                try {
                    final int columnIndex = rsmi.getPosition(s[0], s[1]);
                    this.ioc[i] = db.getIOController(rsmi, columnIndex);
                } catch (SQLException ex) {
                    this.ioc[i] = null;
                }
            } else {
                try {
                    final int columnIndex = rsmi.getPosition(variable);
                    this.ioc[i] = db.getIOController(rsmi, columnIndex);
                } catch (SQLException ex) {
                    this.ioc[i] = null;
                }
            }
            i++;
        }
    }
