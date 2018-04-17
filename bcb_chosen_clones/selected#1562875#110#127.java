        public void instantiate(String str) {
            if ("null".equals(str)) {
                o = null;
                return;
            }
            try {
                if (c == int.class) {
                    o = Integer.parseInt(str);
                } else if (c == Integer.class) {
                    o = new Integer(str);
                } else {
                    Constructor con = c.getConstructor(String.class);
                    o = con.newInstance(str);
                }
            } catch (Exception e) {
                fail(e);
            }
        }
