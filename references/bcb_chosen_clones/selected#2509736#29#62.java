    public Monomio(String aMonomio) {
        coeficiente = 1;
        subindice = 1;
        exponente = 1;
        String regex = "([-\\+]*\\d*[a-z])";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(aMonomio);
        while (m.find()) {
            int start = m.start();
            int end = m.end() - 1;
            String c = aMonomio.substring(start, end);
            c = c.contains("+") ? c.replace("+", "") : c;
            c = c.isEmpty() ? "1" : c;
            int value = Integer.parseInt(c);
            coeficiente = value;
        }
        regex = "([a-z])";
        p = Pattern.compile(regex);
        m = p.matcher(aMonomio);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            char value = aMonomio.substring(start, end).charAt(0);
            variable = value;
        }
        regex = "[a-z]\\d+";
        p = Pattern.compile(regex);
        m = p.matcher(aMonomio);
        while (m.find()) {
            int start = m.start() + 1;
            int value = Integer.parseInt(aMonomio.substring(start));
            subindice = value;
        }
    }
