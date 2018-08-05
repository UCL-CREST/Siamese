    public stock(String ticker) {
        try {
            URL url = new URL("http://finance.yahoo.com/q?s=" + ticker + "&d=v1");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            StringBuffer page = new StringBuffer(8192);
            while ((line = reader.readLine()) != null) {
                page.append(line);
            }
            LispInterpreter lisp = InterpreterFactory.getInterpreter();
            lisp.eval("(load \"nregex\")");
            String quote = lisp.eval("(second (regex \"<b>([0-9][0-9]\\.[0-9][0-9])</b>\" \"" + cleanse(page) + "\"))");
            System.out.println("Current quote: " + quote);
            lisp.exit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
