    public static String substituiAtributoTag(String html, String tag, String name, String atributo, String novoValor) {
        String reg = "<" + tag + "(\\s|\\s[^<]*?\\s)" + atributo + "=\".*?\"(.*?)>";
        Pattern pat = Pattern.compile(reg, Pattern.DOTALL);
        Matcher mat = pat.matcher(html);
        while (mat.find()) {
            if (mat.group().indexOf("name=\"" + name + "\"") != -1) {
                String ini = mat.group(1);
                String fim = mat.group(2);
                int st = mat.start();
                int en = mat.end();
                html = html.substring(0, st) + "<" + tag + ini + atributo + "=\"" + novoValor + "\" " + fim + ">" + html.substring(en);
                break;
            }
        }
        return html;
    }
