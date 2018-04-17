    public String compile(String code, String name, boolean only) {
        if (only) {
            displayWait();
        }
        String output = "";
        String error = "";
        FileOutputStream out;
        PrintStream p;
        try {
            out = new FileOutputStream(name + ".java");
            p = new PrintStream(out);
            p.println(code);
            p.close();
            out.close();
            Process p1 = Runtime.getRuntime().exec("javac " + name + ".java");
            InputStream in = p1.getInputStream();
            InputStream err = p1.getErrorStream();
            int c = 0;
            int d = 0;
            c = in.read();
            d = err.read();
            while (c != -1) {
                output = output + (char) c;
                c = in.read();
            }
            while (d != -1) {
                error = error + (char) d;
                d = err.read();
            }
            in.close();
            err.close();
        } catch (Exception e) {
            System.out.println("error");
        }
        if (only) {
            if (error.equals("")) {
                displayResult("Compilation result", "Compilation sucessful");
            } else {
                displayResult("Compilation result", error);
            }
        }
        return error;
    }
