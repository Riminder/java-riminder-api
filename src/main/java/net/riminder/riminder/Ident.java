package net.riminder.riminder;

public abstract class Ident {

    String name;
    String value;
    String prefix;

    static final public String Filter = "filter";
    static final public String Profile = "profile";

    Ident(String n, String v, String p)
    {
        name = n;
        value = v;
        prefix = p;
    }

    public String getName()
    {
        return prefix + "_" +name;
    }

    public String getValue()
    {
        return value;
    }

    public void setPrefix(String pre)
    {
        prefix = pre;
    }

    public void setValue(String val)
    {
        value = val;
    }

    public static class Reference extends Ident
    {
        public Reference(String pre, String val)
        {
            super("reference", val, pre);
        }
    }

    public static class ID extends Ident
    {
        public ID(String pre, String val)
        {
            super("id", val, pre);
        }
    }
}
