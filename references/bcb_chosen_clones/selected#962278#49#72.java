    private void buildMethod(Method result, String body) {
        Matcher stat = Pattern.compile(String.format("(%s)|(%s)|(%s)", methodReturn.toString(), objectAccess.toString(), staticAccess.toString())).matcher(body);
        int currentPos = 0;
        while (stat.find()) {
            Matcher returns = methodReturn.matcher(stat.group());
            Matcher oAccess = objectAccess.matcher(stat.group());
            Matcher sAccess = staticAccess.matcher(stat.group());
            returns.find();
            oAccess.find();
            sAccess.find();
            if (returns.matches()) {
                result.append(new UnchangedCode(body.substring(currentPos, stat.start() + returns.start(1))));
                result.append(new Method.Return(returns.group(2)));
            } else if (oAccess.matches()) {
                result.append(new UnchangedCode(body.substring(currentPos, stat.start() + oAccess.start(1))));
                result.append(new Method.ObjectAccess(oAccess.group(2), oAccess.group(3), oAccess.group(4)));
            } else {
                result.append(new UnchangedCode(body.substring(currentPos, stat.start() + sAccess.start(1))));
                result.append(new Method.StaticAccess(sAccess.group(2), sAccess.group(3)));
            }
            currentPos = stat.end();
        }
        result.append(new UnchangedCode(body.substring(currentPos, body.length())));
    }
