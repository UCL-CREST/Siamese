    public ClanSessionable readObject() throws Exception {
        Class[] paramTypes = new Class[1];
        paramTypes[0] = getClass();
        Constructor c = Class.forName(readString()).getConstructor(paramTypes);
        ClanSession[] param = new ClanSession[1];
        param[0] = this;
        ClanSessionable ret = (ClanSessionable) c.newInstance(param);
        anchorList.add(ret);
        return ret;
    }
