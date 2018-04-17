                    public Object run() {
                        try {
                            Constructor c = ClassUtils.getConstructorWithArguments(cls, new Class[] {});
                            c.setAccessible(true);
                            Object obj = c.newInstance(new Object[] {});
                            String[] fieldNames = JSONObject.getNames(jsonobj);
                            for (int i = 0; i < jsonobj.length(); i++) {
                                if (!fieldNames[i].equals("class")) {
                                    Field field = cls.getField(fieldNames[i]);
                                    field.setAccessible(true);
                                    field.set(obj, jsonobj.get(fieldNames[i]));
                                }
                            }
                            return obj;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
