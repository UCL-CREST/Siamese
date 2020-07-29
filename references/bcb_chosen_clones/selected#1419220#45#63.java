    public static void main(String[] args) throws ScriptException, IOException {
        Pattern pattern = Pattern.compile("('.*')|(\".*\")|[^\\s]*");
        String template = "/bin/sh \"first argument\" -c 'echo application~{App} Method=~{Method} data=~{Start Data} ave~{Ave} txCount=~{TxCount}'";
        Matcher matcher = pattern.matcher(template);
        List<String> list = new ArrayList<String>();
        while (matcher.find()) {
            list.add(template.substring(matcher.start(), matcher.end()));
        }
        String[] cmd = (String[]) list.toArray(new String[list.size()]);
        for (String arg : cmd) {
            if (arg.length() > 0) {
                byte[] data = arg.getBytes();
                if (data[0] == '\'' && data[data.length - 1] == '\'' || data[0] == '"' && data[data.length - 1] == '"') {
                    arg = new String(data, 1, data.length - 2);
                }
            }
            System.out.println(arg);
        }
    }
