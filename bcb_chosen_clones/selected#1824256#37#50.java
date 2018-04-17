    public Process connect() throws IOException {
        Process p = Runtime.getRuntime().exec(command);
        new Warner(p.getErrorStream());
        OutputStream in = p.getOutputStream();
        OutputStreamWriter write = new OutputStreamWriter(in);
        for (int i = 0; i < args.size(); i++) {
            String s = (String) args.elementAt(i);
            if (i + 1 == args.size()) s += "  --passive";
            write.write(s + "\n");
            if (Debug.check(Bug.PASSIVE)) Debug.println(Bug.PASSIVE, "~~~:" + s);
        }
        write.flush();
        return p;
    }
