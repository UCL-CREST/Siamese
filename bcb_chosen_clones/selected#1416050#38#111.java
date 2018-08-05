    public StringBuffer removeContent(StringBuffer input) {
        content = new ArrayList<String>();
        StringBuffer out;
        Pattern po = Pattern.compile(patternStart);
        Pattern pc = Pattern.compile(patternEnd);
        Matcher mo = po.matcher(input);
        Matcher mc = pc.matcher(input);
        TreeSet<Position> map = new TreeSet<Placeholder.Position>(new Comparator<Position>() {

            public int compare(Position o1, Position o2) {
                return o1._start - o2._start;
            }

            ;
        });
        while (mo.find()) {
            map.add(new Position(mo.start(), mo.end(), Position.Type.opening));
        }
        while (mc.find()) {
            map.add(new Position(mc.start(), mc.end(), Position.Type.closing));
        }
        int depth = 0;
        Position prev = null;
        for (Position p : map) {
            depth += p._type.depthDelta;
            p._depth = depth;
            prev = p;
        }
        boolean stop;
        boolean updatePrev;
        do {
            stop = true;
            prev = null;
            for (Position p : map) {
                updatePrev = !p._ignore;
                if (prev != null && !p._ignore) {
                    if (prev._type == Position.Type.opening && p._type == Position.Type.closing) {
                        if (p._depth > 0) {
                            p._ignore = true;
                            prev._ignore = true;
                            stop = false;
                        }
                    }
                }
                if (updatePrev) {
                    prev = p;
                } else {
                }
            }
        } while (!stop);
        if (map.size() > 0) {
            int last = 0;
            out = new StringBuffer();
            prev = null;
            for (Position p : map) {
                if (prev != null && !p._ignore) {
                    assert !prev._ignore;
                    if (prev._type == Position.Type.opening && p._type == Position.Type.closing) {
                        content.add(input.substring(prev._end, p._start));
                        out.append(input.substring(last, prev._end));
                        last = p._start;
                        p = null;
                    }
                }
                if (p == null || !p._ignore) {
                    prev = p;
                }
            }
            out.append(input.substring(last));
        } else {
            out = input;
        }
        return out;
    }
