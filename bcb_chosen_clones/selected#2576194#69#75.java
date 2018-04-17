    public void addRole(String role) {
        int arrayLength = Array.getLength(this.roles);
        String[] newArray = (String[]) Array.newInstance(this.roles.getClass().getComponentType(), arrayLength + 1);
        System.arraycopy(this.roles, 0, newArray, 0, arrayLength);
        newArray[arrayLength] = role;
        this.roles = newArray;
    }
