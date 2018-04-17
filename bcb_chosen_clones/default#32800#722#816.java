    public void analyzeServerMessage(String incomming) {
        if (incomming.startsWith("Sound ") && incomming.length() > 6) {
            StringTokenizer tokener = new StringTokenizer(incomming.substring(6, incomming.length()), ";");
            while (tokener.hasMoreTokens()) {
                Scanner S = new Scanner(tokener.nextToken());
                String sound = S.next();
                int x = S.nextInt();
                int y = S.nextInt();
                if (x == -1 && y == -1) {
                    sfx.playSound(sound);
                } else if ((x >= left && x <= left + 700) && (y >= top && y <= top + 700)) {
                    sfx.playSound(sound);
                    System.err.println("Playing sound..." + sound + " x " + x + " y " + y);
                }
            }
        } else if (incomming.startsWith("MP3 ")) {
            playMP3(incomming.substring(4, incomming.length()));
        } else if (incomming.startsWith("Message ")) {
            Scanner S = new Scanner(incomming.substring(8, incomming.length()));
            messages.add(new Message(S.nextInt(), S.nextLine()));
            Field.updateUI();
        } else if (incomming.startsWith("Message2")) {
            Scanner S = new Scanner(incomming.substring(9, incomming.length()));
            messages.add(new Message(S.nextInt(), S.nextLine(), true));
            chart = true;
            Field.updateUI();
        } else if (incomming.startsWith("Size")) {
            this.worldSize = Integer.parseInt(incomming.substring(4, incomming.length()));
            initializeChains();
            inializeElevation();
            initializeBlobs();
        } else if (incomming.startsWith("Center")) {
            Scanner S = new Scanner(incomming.substring(6, incomming.length()));
            if (infoing) {
                this.left = S.nextInt() - 350;
                this.top = S.nextInt() - 350;
            }
        } else if (incomming.startsWith("Update")) {
            StringTokenizer tokener = new StringTokenizer(incomming.substring(7, incomming.length()), ";");
            int index = 0;
            while (tokener.hasMoreTokens()) {
                Scanner S = new Scanner(tokener.nextToken());
                int color = Integer.parseInt(S.next());
                time = Integer.parseInt(S.next());
                int theScore = Integer.parseInt(S.next());
                if (theScore > maxScore) maxScore = theScore;
                if (scores.size() <= index) {
                    ArrayList<ScorePoint> theList = new ArrayList<ScorePoint>();
                    scores.add(theList);
                }
                scores.get(index).add(new ScorePoint(time, theScore, color));
                if (index == 0) {
                    Score.setText("Score " + theScore);
                    Time.setText("Time " + time);
                    if (theScore < 100) Score.setForeground(Color.RED); else if (theScore >= 100 && theScore < 200) Score.setForeground(Color.ORANGE); else if (theScore >= 200 && theScore < 400) Score.setForeground(Color.BLACK); else if (theScore >= 400 && theScore < 600) Score.setForeground(Color.GREEN); else Score.setForeground(Color.GREEN.brighter());
                    double Evo = Double.parseDouble(S.next());
                    if (Evo < 150) Power.setForeground(Color.RED); else if (Evo >= 150 && Evo < 500) Power.setForeground(Color.ORANGE); else if (Evo >= 500 && Evo < 1000) Power.setForeground(Color.BLACK); else if (Evo >= 1000 && Evo < 2000) Power.setForeground(Color.GREEN); else Power.setForeground(Color.GREEN.brighter());
                    Power.setText("Evolution " + (int) Evo);
                    Time.updateUI();
                    Score.updateUI();
                    Power.updateUI();
                    if (S.hasNext() && infoing) {
                        String d = S.next();
                        while (S.hasNext()) {
                            d += " " + S.next();
                        }
                        try {
                            description.setText(d);
                            description.updateUI();
                        } catch (NullPointerException e) {
                        }
                    }
                }
                index++;
            }
        } else if (incomming.startsWith("Circles")) {
            StringTokenizer tokener = new StringTokenizer(incomming.substring(8, incomming.length()), ";");
            while (tokener.hasMoreTokens()) {
                Scanner S = new Scanner(tokener.nextToken());
                int id = S.nextInt();
                double x = S.nextDouble();
                double y = S.nextDouble();
                double size = S.nextDouble();
                int color = S.nextInt();
                if (size >= 8) {
                    addCircle(new drawCircle(x, y, size, color, id));
                } else {
                    removeWithId(id);
                }
            }
            UpdateChains();
            minimap.updateUI();
            Field.updateUI();
        }
    }
