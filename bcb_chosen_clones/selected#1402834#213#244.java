    private static void deserialize(GameLogic logic, ByteArrayReader bar) throws Exception {
        ServerEventCommand sec;
        byte sid;
        int len;
        Class sec_class = null;
        Class[] constr_arg_classes = { bar.getClass() };
        Object[] constr_arg = { bar };
        while (bar.available() > 0) {
            try {
                sec_class = null;
                sec = null;
                sid = bar.readByte();
                len = bar.readInt();
                for (int index = 0; index < COMMAND_CLASSES.length; index++) {
                    sec_class = Class.forName(COMMAND_PACKAGE + "." + COMMAND_CLASSES[index]);
                    if (sec_class.getField("SID").getByte(null) == sid) {
                        break;
                    }
                }
                if (sec_class != null) {
                    sec = (ServerEventCommand) sec_class.getConstructor(constr_arg_classes).newInstance(constr_arg);
                    if (logic != null) {
                        sec.action(logic);
                    }
                } else {
                    bar.skip(len);
                }
            } catch (Exception e) {
                Log.error(DESERIALIZE_ERROR + "\n" + e + "\n");
            }
        }
    }
