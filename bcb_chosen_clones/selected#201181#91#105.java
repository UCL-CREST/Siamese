    public static void convertExpression(IModuleManager manager, Module module, StringBuffer sb, String expression, String dataType) {
        Pattern pattern = Pattern.compile("\\$[\\w\\\\\\[\\]]*[\\s*\\.]?");
        Matcher matcher = pattern.matcher(expression);
        int offset = 0;
        while (matcher.find()) {
            String path = expression.substring(matcher.start() + 1, matcher.end());
            if (!path.endsWith(".")) {
                sb.append(expression.substring(offset, matcher.start()));
                convertPath(manager, module, sb, path + ".getValue(");
                sb.append(")");
                offset = matcher.end();
            }
        }
        if (offset == 0 && "BigDecimal".equals(dataType)) sb.append("new BigDecimal(").append(expression).append(")"); else sb.append(expression.substring(offset));
    }
