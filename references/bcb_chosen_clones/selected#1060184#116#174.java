    protected void createContent() {
        out.println("\t\t<div class=\"header\">" + input.getName() + "</div>");
        if (displayLineNumbers) {
            out.println("\t\t<div class=\"lineNumber\">");
            out.print("\t\t\t");
            for (int i = 1; i < lineCount; i++) {
                out.print(i);
                if (i < lineCount - 1) out.print("<br />");
            }
            out.println("\n\t\t</div>");
        }
        out.print("\t\t<div class=\"code\">");
        try {
            BufferedReader in = new BufferedReader(new FileReader(input));
            String lineBuffer = in.readLine();
            int end = 0;
            while (lineBuffer != null) {
                Pattern pattern;
                Matcher matcher;
                if (lineBuffer.trim().length() > 0) {
                    buffer.append("\n\t\t\t");
                    if (lineBuffer.trim().charAt(0) != ';') {
                        for (int i = 0; i < core.getSyntaxParser().elementCount; i++) {
                            if (i != core.getSyntaxParser().TYPE_COMMENT) {
                                pattern = Pattern.compile(core.getSyntaxParser().getRegularExpression(i), Pattern.CASE_INSENSITIVE);
                                matcher = pattern.matcher("\n" + lineBuffer.toUpperCase().split(";")[0] + "\n");
                                String restOfLine = null;
                                while (matcher.find()) {
                                    end = matcher.end();
                                    try {
                                        if (restOfLine != null) {
                                        }
                                        buffer.append("<span class=\"" + core.getSyntaxParser().syntaxData[i][0] + "\">" + lineBuffer.substring(matcher.start(), matcher.end() - 1) + "</span>");
                                        System.out.println("REST:" + lineBuffer.substring(matcher.end() - 1, lineBuffer.length()));
                                    } catch (java.lang.StringIndexOutOfBoundsException e) {
                                        buffer.append("-- DEBUG --> \"" + lineBuffer + "\" start:" + matcher.start() + " end:" + matcher.end() + "<br />");
                                    }
                                }
                            }
                        }
                    }
                }
                pattern = Pattern.compile(core.getSyntaxParser().getRegularExpression(core.getSyntaxParser().TYPE_COMMENT));
                matcher = pattern.matcher(lineBuffer);
                if (matcher.find()) {
                    end = lineBuffer.length();
                    buffer.append("<span class=\"comments\">" + lineBuffer + "</span>");
                }
                if (end < lineBuffer.length()) buffer.append(lineBuffer.substring(end, lineBuffer.length()));
                if (--lineCount > 1) buffer.append("\n\t\t\t<br />");
                lineBuffer = in.readLine();
            }
        } catch (java.io.FileNotFoundException e) {
        } catch (java.io.IOException e) {
        }
        String output = buffer.toString();
        output = output.replaceAll(" (?!/>|class=)", HTML_SPACE);
        out.println(output + "\n\t\t</div>");
    }
