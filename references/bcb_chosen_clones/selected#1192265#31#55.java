    public void newRace() {
        int random = 0;
        for (int i = 0; i < 8; i++) {
            int id = 31003;
            random = Rnd.get(24);
            while (true) {
                for (int j = i - 1; j >= 0; j--) {
                    if (_monsters[j].getTemplate().npcId == (id + random)) {
                        random = Rnd.get(24);
                        continue;
                    }
                }
                break;
            }
            try {
                L2NpcTemplate template = NpcTable.getInstance().getTemplate(id + random);
                _constructor = Class.forName("com.l2jserver.gameserver.model.actor.instance." + template.type + "Instance").getConstructors()[0];
                int objectId = IdFactory.getInstance().getNextId();
                _monsters[i] = (L2Npc) _constructor.newInstance(objectId, template);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        newSpeeds();
    }
