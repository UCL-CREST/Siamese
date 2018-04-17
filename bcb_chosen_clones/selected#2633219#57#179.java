    public void doQuery(String keyword, final int page) {
        this.keyword = keyword;
        keyword = keyword.replace(' ', '+');
        commentText = new JTextArea(10, 80);
        final Vector commentVector = new Vector();
        int matchingDocCount = 0;
        int hitCount = 0;
        getContentPane().removeAll();
        Vector linkVector = new Vector();
        try {
            String featureid = keyword;
            URL connectURL = new URL("http://www.ensembl.org/Homo_sapiens/textview?idx=External&q=" + keyword + "&page=" + page);
            InputStream urlStream = connectURL.openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlStream));
            String line, link, content, label, head = null;
            Box tabBox = null;
            String linkstr;
            String comment = "";
            int EnsExtCount;
            int EnsGeneCount;
            while ((line = reader.readLine()) != null) {
                if (line.indexOf("documents match your query") != -1) {
                    matchingDocCount = Integer.parseInt(line.substring(line.indexOf("<B>") + 3, line.indexOf("</B>")));
                    continue;
                }
                if (line.indexOf("matches in the Ensembl External index") != -1) {
                }
                if (line.indexOf("matches in the Ensembl Gene index:") != -1) {
                }
                if (line.indexOf("Homo_sapiens/geneview?gene") != -1) {
                    if (line.indexOf("www.ensembl.org") != -1) {
                        line = line.substring(line.indexOf("www.ensembl.org"));
                        line = line.substring(line.indexOf("</A>") + 4);
                    }
                    int linkStart = line.indexOf("Homo_sapiens/geneview?gene");
                    if (linkStart == -1) break;
                    linkstr = "http://www.ensembl.org/" + line.substring(linkStart, line.indexOf("\">"));
                    line = line.substring(line.indexOf("</A>") + 4);
                    StringBuffer chars = new StringBuffer(line.length());
                    boolean inTag = false;
                    boolean inEntity = false;
                    boolean firstBRTossed = false;
                    line = line.substring(line.indexOf("<"));
                    for (int ch = 0; ch < line.length(); ch++) {
                        if (line.charAt(ch) == '<') {
                            inTag = true;
                            if ((line.charAt(ch + 1) == 'b' || line.charAt(ch + 1) == 'B') && (line.charAt(ch + 2) == 'r' || line.charAt(ch + 2) == 'R')) {
                                if (firstBRTossed) {
                                    chars.append("\n");
                                } else {
                                    firstBRTossed = true;
                                }
                            }
                        }
                        if (line.charAt(ch) == '&') inEntity = true;
                        if (!inTag && !inEntity) chars.append(line.charAt(ch));
                        if (line.charAt(ch) == ';') inEntity = false;
                        if (line.charAt(ch) == '>') inTag = false;
                    }
                    comment = chars.toString();
                    commentVector.add(comment);
                    linkVector.add(linkstr);
                    hitCount++;
                }
            }
            if (hitCount == 0) commentText.setText("No Matches Found for " + keyword);
            commentText.setLineWrap(true);
            commentText.setWrapStyleWord(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "No Matches Found for " + keyword);
            return;
        }
        final JList lst = new JList(linkVector);
        lst.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                int ind = lst.getSelectedIndex();
                commentText.setText((String) commentVector.elementAt(ind));
                commentText.select(0, 0);
            }
        });
        MouseListener mouseListener = new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = lst.locationToIndex(e.getPoint());
                    extractEnsemblCoords((String) lst.getModel().getElementAt(index));
                }
            }
        };
        lst.addMouseListener(mouseListener);
        lst.setSelectedIndex(0);
        scrollPane = new JScrollPane(commentText);
        JPanel pagePanel = new JPanel();
        final JButton prevBttn = new JButton("<=");
        final JButton nextBttn = new JButton("=>");
        prevBttn.setEnabled(page > 1);
        nextBttn.setEnabled(page + hitCount < matchingDocCount);
        ActionListener pageHandler = new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                if (ae.getSource() == prevBttn) {
                    doQuery(EnsemblLookup.this.keyword, page - 20);
                } else {
                    doQuery(EnsemblLookup.this.keyword, page + 20);
                }
            }
        };
        pagePanel.add(prevBttn);
        prevBttn.addActionListener(pageHandler);
        pagePanel.add(nextBttn);
        nextBttn.addActionListener(pageHandler);
        JPanel hitsAndTextPanel = new JPanel();
        hitsAndTextPanel.setLayout(new GridLayout(2, 1));
        hitsAndTextPanel.add(new JScrollPane(lst));
        hitsAndTextPanel.add(scrollPane);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(hitsAndTextPanel, BorderLayout.CENTER);
        getContentPane().add(pagePanel, BorderLayout.SOUTH);
        setTitle("Results for " + keyword + "  Displaying " + (page + 1) + ((hitCount > 1) ? (" - " + (page + hitCount)) : "") + " of " + matchingDocCount);
        show();
    }
