    public void run() {
        String s;
        s = "";
        try {
            URL url = new URL("http://www.askoxford.com/concise_oed/" + word.toLowerCase() + "?view=uk");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String str;
            while (((str = in.readLine()) != null) && (!stopped)) {
                s = s + str;
            }
            in.close();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        Pattern pattern = Pattern.compile("<h2>" + word.toLowerCase() + "(.+?)<p><a href", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(s);
        java.io.StringWriter wr = new java.io.StringWriter();
        HTMLDocument doc = null;
        HTMLEditorKit kit = (HTMLEditorKit) editor.getEditorKit();
        try {
            doc = (HTMLDocument) editor.getDocument();
        } catch (Exception e) {
        }
        System.out.println(wr);
        editor.setContentType("text/html");
        if (matcher.find()) try {
            kit.insertHTML(doc, editor.getCaretPosition(), "<HR>" + matcher.group(1) + "<HR>", 0, 0, null);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } else try {
            kit.insertHTML(doc, editor.getCaretPosition(), "<HR><FONT COLOR='RED'>NOT FOUND!!</FONT><HR>", 0, 0, null);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        button.setEnabled(true);
    }
