    public String getTargetCompilationUnitContents() {
        String result = null;
        if (getControlModel().getFacadeHelper() != null && (!targetCompilationUnitExists || !targetCompilationChanged)) {
            result = getControlModel().getFacadeHelper().getOriginalContents(targetCompilationUnit);
        }
        if (result == null) {
            result = targetCompilationUnit.getContents();
        }
        if (fixInterfaceBrace) {
            if (interfaceBracePattern == null) {
                interfaceBracePattern = Pattern.compile("(?:\\n\\r|\\r\\n|\\n|\\r)(\\s*)(?:public|private|protected|static|\\s)*(?:interface|class)\\s*[^\\{\\n\\r]*(\\{)(\\n\\r|\\r\\n|\\n|\\r)", Pattern.MULTILINE);
            }
            Matcher matcher = interfaceBracePattern.matcher(result);
            int offset = 0;
            while (matcher.find()) {
                if (getControlModel().standardBraceStyle) {
                    if (result.charAt(matcher.start(2) - 1) != ' ') {
                        result = result.substring(0, offset + matcher.start(2)) + " {" + result.substring(offset + matcher.end(2), result.length());
                        offset += 1;
                    }
                } else {
                    result = result.substring(0, offset + matcher.start(2)) + matcher.group(3) + matcher.group(1) + "{" + result.substring(offset + matcher.end(2), result.length());
                    offset += matcher.group(1).length() + matcher.group(3).length();
                }
            }
        }
        return result;
    }
