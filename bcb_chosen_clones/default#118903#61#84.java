    public void comrun(String code, String name) {
        displayWait();
        String comres = compile(code, name, false);
        System.out.println(comres);
        if (!comres.equals("")) {
            displayResult("Code compilation", comres);
        } else {
            String output = "";
            try {
                Process p2 = Runtime.getRuntime().exec("java " + name);
                InputStream in = p2.getInputStream();
                int c = 0;
                c = in.read();
                while (c != -1) {
                    output = output + (char) c;
                    c = in.read();
                }
                in.close();
                displayResult("Code output", "Compilation sucessful\n\n" + output);
            } catch (Exception e) {
                System.out.println("error");
            }
        }
    }
