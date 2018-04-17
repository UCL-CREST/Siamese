    public final String getKey() {
        if (!updateNeed) return currentKey;
        String key = "";
        key += "ID:" + getCard().getUniqueID();
        key += " " + getNowColor();
        key += " " + getNowPower();
        key += " " + getNowToughness();
        key += " " + preventedDamage;
        key += " Abilities: " + getNowCardAbilities();
        key += " Tapped: " + isTapped();
        key += " Sick: " + isSick();
        key += " Attacker: " + isAttacker;
        key += " Blocker: " + isBlocker;
        CRC32 c = new CRC32();
        c.update(key.getBytes());
        currentKey = "" + c.getValue();
        return currentKey;
    }
