    private void readFromCommandline() {
        BufferedReader l_input = new BufferedReader(new InputStreamReader(System.in));
        String l_message = new String();
        String l_tempString = new String();
        int l_targetPlayer = -1;
        while (m_commandLineReading) {
            System.out.print("\nType help for help \n>");
            try {
                l_message = l_input.readLine();
            } catch (Exception ex) {
                logger.severe("Exception caught in JaisTeam::readFromCommandline:" + ex.getMessage());
            }
            if (l_message.equalsIgnoreCase("Exit") || l_message.equalsIgnoreCase("E")) {
                m_commandLineReading = false;
                for (ListIterator it = m_playerList.listIterator(); it.hasNext(); ) {
                    Jais l_tempJais = (Jais) it.next();
                    l_tempJais.quit();
                }
            } else if (l_message.equalsIgnoreCase("help")) System.out.println("\nCommands are:" + "\n[Playernumber] visualization [ON | OFF]" + "\n[Playernumber] reconnect" + "\n[Playernumber] exit for kicking one player" + "\n\"Exit\" for exiting the game"); else {
                StringTokenizer l_tokenizer = new StringTokenizer(l_message);
                try {
                    l_targetPlayer = Integer.parseInt(l_tokenizer.nextToken());
                    if (!(l_targetPlayer > 0 && l_targetPlayer <= m_numberOfPlayers)) System.out.println("No such Player! (1..n)"); else {
                        l_tempString = "";
                        while (l_tokenizer.hasMoreTokens()) l_tempString += " " + l_tokenizer.nextToken();
                        ((Jais) m_playerList.get(l_targetPlayer - 1)).parseKbInfo(l_tempString);
                        logger.finest("Parsed \"" + l_tempString + "\" to Player " + l_targetPlayer + "\n");
                    }
                } catch (Exception ex) {
                    System.out.println("Didn't understand your command. Please retry (" + ex + ")\n");
                }
            }
        }
    }
