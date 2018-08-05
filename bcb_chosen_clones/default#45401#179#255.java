    public RequestMessage createRequestMessage(String cmdLine, StringBuffer output) {
        StringTokenizer strtok = new StringTokenizer(cmdLine);
        String cmdName = strtok.nextToken();
        try {
            MessageInfo msgInfo = (MessageInfo) msgMap.get(cmdName);
            if (msgInfo != null) {
                Class msgClass = (Class) msgInfo.getMsgClass();
                List<String> argList = new ArrayList<String>();
                while (strtok.hasMoreTokens()) {
                    String token = strtok.nextToken();
                    token = token.replace('_', ' ');
                    argList.add(token);
                }
                Constructor[] constrs = msgClass.getConstructors();
                Constructor constr = null;
                for (int i = 0; i < constrs.length; i++) {
                    if (constrs[i].getParameterTypes().length == argList.size()) {
                        constr = constrs[i];
                        break;
                    }
                }
                if (constr == null) {
                    output.append("Invalid number of arguments" + CR);
                    output.append("Command syntax:" + CR);
                    output.append(getMsgHelp(cmdName) + CR);
                    return null;
                }
                Object args[] = new Object[argList.size()];
                Class[] argTypes = constr.getParameterTypes();
                Iterator iter = argList.iterator();
                for (int i = 0; i < argTypes.length; i++) {
                    String arg = (String) iter.next();
                    Class argType = argTypes[i];
                    if (argType == Integer.TYPE) args[i] = Integer.valueOf(arg); else if (argType == Double.TYPE) args[i] = Double.valueOf(arg); else if (argType == String.class) args[i] = arg; else if (Enum.class.isAssignableFrom(argType)) {
                        Field field = argType.getField("metaInfo");
                        EnumInfo enumInfo = (EnumInfo) field.get(argType);
                        Enum enumeration = enumInfo.getByUserLabel(arg);
                        args[i] = enumeration;
                    } else if (argType == Boolean.TYPE) {
                        args[i] = Boolean.valueOf(arg);
                    } else if (argType == Calendar.class) {
                        long time = Long.parseLong(arg);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(time);
                        args[i] = calendar;
                    } else if (argType.isArray() && argType.getComponentType() == Integer.TYPE) {
                        String[] strValues = arg.split(",");
                        int[] values = new int[strValues.length];
                        for (int j = 0; j < strValues.length; j++) {
                            int intValue = Integer.valueOf(strValues[j]);
                            values[j] = intValue;
                        }
                        args[i] = values;
                    } else output.append("Unknown type: " + argType + CR);
                }
                RequestMessage msg = (RequestMessage) constr.newInstance(args);
                return msg;
            } else {
                output.append("Invalid command: " + cmdName + CR);
            }
        } catch (NumberFormatException nfe) {
            output.append("Invalid argument types supplied" + CR);
            output.append("Command syntax:" + CR);
            output.append(getMsgHelp(cmdName) + CR);
        } catch (InvocationTargetException ite) {
            output.append("Invalid argument types supplied" + CR);
            output.append("Command syntax:" + CR);
            output.append(getMsgHelp(cmdName) + CR);
        } catch (NoSuchFieldException nsfe) {
            nsfe.printStackTrace();
        } catch (IllegalAccessException iae) {
            iae.printStackTrace();
        } catch (InstantiationException ie) {
            ie.printStackTrace();
        }
        return null;
    }
