    public Collection<ParameterColumnMapping> replaceIncremental(String parameterListName) throws ParseException {
        Pattern p = Pattern.compile("(" + THIS_GET_INCREMENTAL.replace(".", "\\.").replace("(", "\\(") + ").*\\)");
        Matcher m = p.matcher(sql);
        incrementalParameters = new ArrayList<ParameterColumnMapping>();
        try {
            String code = sql;
            while (m.find()) {
                String function = code.substring(m.start(), m.end());
                code = code.replace(function, "${INCPARAM}");
                function = function.replace(THIS_GET_INCREMENTAL, "");
                String param = function.substring(0, function.length() - 1).trim();
                String[] vals = param.split(",");
                IncrementalType type = null;
                if (vals.length == 4) this.offSet = Integer.parseInt(vals[3].trim());
                if (vals.length == 3) {
                    type = IncrementalType.valueOf(vals[2].trim());
                } else type = IncrementalType.UNKNOWN;
                incrementalParameters.add(new ParameterColumnMapping(parameterListName, vals[1].trim(), vals[0].trim(), type));
            }
            sql = code.replace("${INCPARAM}", "?");
            return incrementalParameters;
        } catch (Throwable e) {
            ParseException e1 = new ParseException("Invalid parameters for incremental syntax should be " + THIS_GET_INCREMENTAL + "column name,parameter name) - " + e.getMessage() + ", whilst parsing \"" + sql + "\"", m.start());
            e1.setStackTrace(e.getStackTrace());
            throw e1;
        }
    }
