    private static void compileRuleText(HibernateCompileContext ctx, ClassPrint print, Module module, Rule rule, String codeText, boolean root, boolean replaceReturn) throws FileNotFoundException, IOException, CheckerException {
        HibernateEntityCompile.module = module;
        HibernateEntityCompile.rule = rule;
        int beginIndex = codeText.indexOf("{");
        int endIndex = codeText.lastIndexOf("}");
        codeText = codeText.substring(beginIndex + 1, endIndex);
        if (replaceReturn) codeText = codeText.replaceAll("return\\s*;", "return true;");
        Pattern pattern = Pattern.compile("\\s*(/\\*[^\\*/]*\\*/|//[^\r\n]*[\r\n])");
        Matcher matcher = pattern.matcher(codeText);
        int offset = 0;
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            sb.append(codeText.substring(offset, matcher.start()));
            offset = matcher.end();
        }
        sb.append(codeText.substring(offset));
        codeText = sb.toString();
        pattern = Pattern.compile("#CT[0-9]{6}");
        matcher = pattern.matcher(codeText);
        offset = 0;
        sb = new StringBuffer();
        while (matcher.find()) {
            sb.append(codeText.substring(offset, matcher.start()));
            sb.append("Platform.getI18nString(\"").append(module.getName()).append("\",\"").append(codeText.substring(matcher.start() + 1, matcher.end())).append("\")");
            offset = matcher.end();
        }
        sb.append(codeText.substring(offset));
        codeText = sb.toString();
        CodetextUtils.checkEx(ctx.manager, module, codeText, rule);
        pattern = Pattern.compile("new ICallback\\([\\w]*\\)\\.invoke\\(\\)");
        matcher = pattern.matcher(codeText);
        offset = 0;
        sb = new StringBuffer();
        while (matcher.find()) {
            sb.append(codeText.substring(offset, matcher.start()));
            sb.append("new ICallback(){public Object invoke(Object... arguments){return ").append(codeText.substring(matcher.start() + 14, matcher.end() - 10)).append("(arguments);}}");
            offset = matcher.end();
        }
        sb.append(codeText.substring(offset));
        codeText = sb.toString();
        pattern = Pattern.compile("Platform.exitEvent\\([\\s]*\\)");
        matcher = pattern.matcher(codeText);
        offset = 0;
        sb = new StringBuffer();
        while (matcher.find()) {
            sb.append(codeText.substring(offset, matcher.start()));
            sb.append("return false");
            offset = matcher.end();
        }
        sb.append(codeText.substring(offset));
        codeText = sb.toString();
        pattern = Pattern.compile("Platform.[\\w]*\\(");
        matcher = pattern.matcher(codeText);
        offset = 0;
        sb = new StringBuffer();
        while (matcher.find()) {
            sb.append(codeText.substring(offset, matcher.start()));
            convertMethodName(sb, codeText.substring(matcher.start(), matcher.end()), root);
            offset = matcher.end();
        }
        sb.append(codeText.substring(offset));
        codeText = sb.toString();
        pattern = Pattern.compile("\\$[\\w\\\\\\[\\]]*\\s*=[^\\;]*\\;");
        matcher = pattern.matcher(codeText);
        offset = 0;
        sb = new StringBuffer();
        while (matcher.find()) {
            sb.append(codeText.substring(offset, matcher.start()));
            String body = codeText.substring(matcher.start() + 1, matcher.end());
            String[] values = body.split("=");
            String path = values[0].trim();
            convertPath(ctx.manager, module, sb, path + ".setValue(");
            String dataType = typeName((Datafield) ctx.manager.getEibsObject(module, path));
            convertExpression(ctx.manager, module, sb, values[1].substring(0, values[1].length() - 1), dataType);
            sb.append(");");
            offset = matcher.end();
        }
        sb.append(codeText.substring(offset));
        codeText = sb.toString();
        pattern = Pattern.compile("\\$[\\w\\\\\\[\\]]*(\\.[s|g]etValue\\()?");
        matcher = pattern.matcher(codeText);
        offset = 0;
        sb = new StringBuffer();
        while (matcher.find()) {
            sb.append(codeText.substring(offset, matcher.start()));
            convertPath(ctx.manager, module, sb, codeText.substring(matcher.start() + 1, matcher.end()));
            offset = matcher.end();
        }
        sb.append(codeText.substring(offset));
        codeText = sb.toString();
        codeText = codeText.replaceFirst("^\\s*", "");
        boolean flag = true;
        for (String line : codeText.split("[\r\n]+")) {
            print.println(line);
            if (flag) {
                print.decIndex();
                flag = false;
            }
        }
        print.incIndex();
    }
