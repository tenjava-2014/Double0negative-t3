package org.mcsg.tenjava.random.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@SuppressWarnings("serial")
public class MultiMapLongTrigger extends HashMap<Long, List<Trigger>> {
		public void put(Long frequency, Trigger trigger) {
	        List<Trigger> current = getOrDefault(frequency, new ArrayList<Trigger>());
	        super.put(frequency, current);
	        current.add(trigger);
	    }
}