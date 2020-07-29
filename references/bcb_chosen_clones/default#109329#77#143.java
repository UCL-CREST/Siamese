    private String removeInvalidChars(String input) {
        String text;
        StringBuffer contents = new StringBuffer();
        BufferedReader reader = null;
        if (isFile) {
            try {
                reader = new BufferedReader(new FileReader(input));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    contents.append(line);
                    contents.append(System.getProperty("line.separator"));
                }
                reader.close();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            text = contents.toString();
        } else {
            text = input;
        }
        Pattern pattern = Pattern.compile("Comment=\"[a-zA-Z0-9 \t\\-\\+\\{\\}!@#$%\\^\\&\\*\\(\\;:\\',./\\?\\\\|<>)]*\"");
        Matcher matcher = pattern.matcher(text);
        boolean found = false;
        StringBuilder newText = new StringBuilder();
        int lastEnd = 0;
        while (matcher.find()) {
            String str = matcher.group();
            str = str.substring(str.indexOf("\"") + 1);
            str = str.substring(0, str.lastIndexOf("\""));
            if (str.contains("&") || str.contains("<") || str.contains(">") || str.contains("'")) {
                if (!found) found = true;
                str = str.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("'", "&apos;").replace("\"", "&quot;");
                newText.append(text.substring(lastEnd, matcher.start()) + "Comment=\"" + str + "\"");
                lastEnd = matcher.end();
            }
        }
        if (found) {
            newText.append(text.substring(lastEnd));
            if (isFile) {
                File temp = null;
                Writer output = null;
                try {
                    temp = File.createTempFile("fbench", "");
                    if (temp != null && temp.exists()) {
                        output = new BufferedWriter(new FileWriter(temp));
                        output.write(newText.toString());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (output != null) {
                        try {
                            output.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                input = temp.getPath();
            } else {
                input = newText.toString();
            }
        }
        return input;
    }
