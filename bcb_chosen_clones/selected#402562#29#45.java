        @Override
        public List<Facteur> exec(Long L) {
            List<Facteur> ret = new ArrayList<Facteur>();
            int ordre = 0;
            for (int npr = 2; npr <= L / npr; npr++) {
                while (L % npr == 0) {
                    ordre++;
                    L = L / npr;
                }
                ret.add(new Facteur(npr, ordre));
                ordre = 0;
            }
            if (L > 1) {
                ret.add(new Facteur(L.intValue(), 1));
            }
            return ret;
        }
