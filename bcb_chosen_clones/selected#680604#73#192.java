    public synchronized boolean setSource(File file) {
        if (!file.isDirectory()) {
            return false;
        }
        m_current = -1;
        Random rand = new Random();
        m_files = file.listFiles(new ValidFileFilter());
        if (m_random) {
            List<File> fileList = Arrays.asList(m_files);
            Collections.shuffle(fileList, rand);
            m_files = fileList.toArray(m_files);
        }
        m_innerCount = 0;
        m_multiStimuliParsers = false;
        List<int[]> orderList = new LinkedList<int[]>();
        int[] order = null;
        StimuliParser parser = new FolderParser(m_maxsize, m_spp, 1, m_controls, false);
        StimuliParser parser2 = null;
        if (m_spp > 1) parser2 = new FolderParser(m_maxsize, m_spp - 1, 1, m_controls, false);
        if (parser.setSource(m_files[0])) {
            List<int[]> singleOrderList = new LinkedList<int[]>();
            m_multiStimuliParsers = true;
            order = new int[2];
            order[0] = 0;
            int thiscount = parser.getInnerStimuliCount();
            for (int i = 0; i < thiscount; ++i) {
                order[1] = i;
                singleOrderList.add(order.clone());
            }
            m_innerCount += thiscount;
            if (m_random) {
                Collections.shuffle(singleOrderList, rand);
            }
            orderList.addAll(singleOrderList);
        } else if (m_spp > 1) {
            File tiedFolder = getTiedFolder(m_files[0]);
            if (tiedFolder.isDirectory()) {
                List<int[]> singleOrderList = new LinkedList<int[]>();
                m_multiStimuliParsers = true;
                parser2.setSource(tiedFolder);
                order = new int[2];
                order[0] = 0;
                int thiscount = parser2.getInnerStimuliCount();
                for (int i = 0; i < thiscount; ++i) {
                    order[1] = i;
                    singleOrderList.add(order.clone());
                }
                m_innerCount += thiscount;
                if (m_random) Collections.shuffle(singleOrderList, rand);
                orderList.addAll(singleOrderList);
            }
        }
        if (m_multiStimuliParsers) {
            for (int i = 1; i < m_files.length; ++i) {
                List<int[]> singleOrderList = new LinkedList<int[]>();
                order[0] = i;
                int thiscount;
                if (!parser.setSource(m_files[i])) {
                    File tiedFolder = getTiedFolder(m_files[i]);
                    parser2.setSource(tiedFolder);
                    thiscount = parser2.getInnerStimuliCount();
                } else thiscount = parser.getInnerStimuliCount();
                for (int j = 0; j < thiscount; ++j) {
                    order[1] = j;
                    singleOrderList.add(order.clone());
                }
                m_innerCount += thiscount;
                if (m_random) {
                    Collections.shuffle(singleOrderList, rand);
                }
                orderList.addAll(singleOrderList);
            }
        } else {
            order = new int[m_spp];
            for (int i = 0; i < m_spp; ++i) order[i] = i;
            int h = 0;
            int choices = 0;
            while (true) {
                orderList.add(order.clone());
                ++choices;
                if (order[h] == m_files.length - m_spp + h) {
                    if (h == 0) break;
                    ++order[--h];
                    for (int i = h + 1; i < m_spp; ++i) order[i] = order[i - 1] + 1;
                } else {
                    h = m_spp - 1;
                    ++order[h];
                }
            }
            m_innerCount = choices;
            if (m_random) Collections.shuffle(orderList, rand);
        }
        if (m_sequences > 1) {
            m_innerCount *= m_sequences;
            List<int[]> holder = new LinkedList<int[]>(orderList);
            for (int i = 1; i < m_sequences; ++i) {
                List<int[]> temp = new LinkedList<int[]>();
                for (int[] v : orderList) temp.add(v.clone());
                Collections.shuffle(temp, rand);
                holder.addAll(temp);
            }
            orderList = holder;
        }
        m_order = orderList.toArray(new int[m_innerCount][order.length]);
        if (!m_multiStimuliParsers && m_random) {
            int temp;
            for (int[] v : m_order) {
                for (int i = 1; i < v.length; ++i) {
                    int swapi = rand.nextInt(i + 1);
                    if (swapi != i) {
                        temp = v[i];
                        v[i] = v[swapi];
                        v[swapi] = temp;
                    }
                }
            }
        }
        m_parsers = new StimuliParser[m_order[0].length];
        return true;
    }
