    protected void analyse() {
        Pattern varPattern = Pattern.compile("(\\$)+\\([^ \\)]*\\)");
        Matcher varMatch = varPattern.matcher(this.input);
        this.variableLocations = new Vector<VariableLocation>();
        while (varMatch.find()) {
            int start = varMatch.start();
            String var = varMatch.group();
            while (var.startsWith("$$")) {
                var = var.substring(2);
                start = start + 2;
            }
            if (var.startsWith("$(")) this.variableLocations.add(new VariableLocation(var.substring(2, var.length() - 1), start, varMatch.end()));
        }
    }
