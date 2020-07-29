    public static void writeToPetrify(TransitionSystem ts, Writer bw) throws IOException {
        File temp = new File("_temp");
        BufferedWriter tw = new BufferedWriter(new FileWriter(temp));
        BufferedReader tr = new BufferedReader(new FileReader(temp));
        HashSet<ModelGraphVertex> sources = new HashSet<ModelGraphVertex>();
        HashSet<ModelGraphVertex> dests = new HashSet<ModelGraphVertex>();
        ArrayList transitions = ts.getEdges();
        HashSet<String> events = new HashSet<String>();
        for (int i = 0; i < transitions.size(); i++) {
            TransitionSystemEdge transition = (TransitionSystemEdge) transitions.get(i);
            events.add(replaceBadSymbols(transition.getIdentifier()));
            sources.add(transition.getSource());
            dests.add(transition.getDest());
            if (ts.getStateNameFlag() == TransitionSystem.ID) {
                tw.write("s" + transition.getSource().getId() + " ");
                tw.write(replaceBadSymbols(transition.getIdentifier()) + " ");
                tw.write("s" + transition.getDest().getId() + "\n");
            } else {
                tw.write(replaceBadSymbols(transition.getSource().getIdentifier()) + " ");
                tw.write(replaceBadSymbols(transition.getIdentifier()) + " ");
                tw.write(replaceBadSymbols(transition.getDest().getIdentifier()) + "\n");
            }
        }
        tw.close();
        bw.write(".model " + ts.getName().replaceAll(" ", "_") + "\n");
        bw.write(".dummy ");
        Iterator it = events.iterator();
        while (it.hasNext()) bw.write(it.next() + " ");
        bw.write("\n");
        bw.write(".state graph" + "\n");
        int c;
        while ((c = tr.read()) != -1) bw.write(c);
        tr.close();
        temp.delete();
        for (ModelGraphVertex dest : dests) {
            if (sources.contains(dest)) {
                sources.remove(dest);
            }
        }
        ModelGraphVertex source = sources.isEmpty() ? null : sources.iterator().next();
        if (ts.getStateNameFlag() == TransitionSystem.ID) {
            if (!ts.hasExplicitEnd()) bw.write(".marking {s0}" + "\n"); else bw.write(".marking {s" + source.getId() + "}\n");
        } else if (source != null) {
            bw.write(".marking {" + replaceBadSymbols(source.getIdentifier()) + "}\n");
        }
        bw.write(".end");
    }
