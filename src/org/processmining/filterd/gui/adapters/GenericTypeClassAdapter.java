package org.processmining.filterd.gui.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class GenericTypeClassAdapter extends XmlAdapter<String, Class<?>> {

	public Class<?> unmarshal(String clsName) throws ClassNotFoundException {
		if (clsName.equals("byte"))
            return byte.class;
        if (clsName.equals("short"))
            return short.class;
        if (clsName.equals("int"))
            return int.class;
        if (clsName.equals("long"))
            return long.class;
        if (clsName.equals("char"))
            return char.class;
        if (clsName.equals("float"))
            return float.class;
        if (clsName.equals("double"))
            return double.class;
        if (clsName.equals("boolean"))
            return boolean.class;
        if (clsName.equals("void"))
        	return void.class;
		return Class.forName(clsName);
	}

	public String marshal(Class<?> cls) {
		return cls.getName();
	}

}
