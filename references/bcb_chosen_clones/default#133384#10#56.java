    public static void main(String[] argv) throws IOException {
        ArrayList<Point> p = new ArrayList<Point>();
        String fn = null;
        boolean p1 = false;
        if (argv.length != 1) {
            System.out.println("Usage: <program_name> filename parallel\n" + "where filename is path to file with points number " + "and their coodinates\n" + "The result is outputted to ./out");
            return;
        } else {
            fn = argv[0];
        }
        StringTokenizer tok = new StringTokenizer("");
        BufferedReader fin = new BufferedReader(new FileReader(fn));
        PrintWriter fout = new PrintWriter("./out");
        int n = 0;
        if (!tok.hasMoreTokens()) {
            tok = new StringTokenizer(fin.readLine());
        }
        n = Integer.parseInt(tok.nextToken());
        for (int i = 0; i < n; ++i) {
            if (!tok.hasMoreTokens()) {
                tok = new StringTokenizer(fin.readLine());
            }
            double x = Double.parseDouble(tok.nextToken());
            double y = Double.parseDouble(tok.nextToken());
            p.add(new Point(x, y, true));
        }
        SteinerTreeSolver solver = new SteinerTreeSolver(p, p1);
        solver.solve();
        ArrayList<Point> minP = solver.minP();
        fout.print(minP.size());
        fout.print(" ");
        fout.println(n);
        for (int i = 0; i < minP.size(); ++i) {
            fout.print(minP.get(i).x);
            fout.print("\t");
            fout.print(minP.get(i).y);
            fout.print("\t");
            fout.print(minP.get(minP.get(i).parent).x);
            fout.print("\t");
            fout.println(minP.get(minP.get(i).parent).y);
        }
        fout.print("Number of reviewed trees: ");
        fout.println(solver.numberOfTrees());
        fout.print("Minimum length: ");
        fout.println(solver.minDist());
        fout.close();
    }
