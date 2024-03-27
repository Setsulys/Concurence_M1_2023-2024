package fr.uge.exam.exo3;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

public class MaxRecorderOne {

	private volatile int max =-1;
	private static final VarHandle MAX_HANDLE;
	
	static {
		var lookup = MethodHandles.lookup();
		try {
			MAX_HANDLE = lookup.findVarHandle(MaxRecorderOne.class, "max", int.class);
		}catch(NoSuchFieldException | IllegalAccessException e) {
			throw new AssertionError(e);
		}
	}
	
	public boolean process(int value) {
		for(;;) {
			int current = this.max;
			if(current < value) {
				if(MAX_HANDLE.compareAndSet(this,current,value)) {
					return true;
				}			
			}else {
				return false;
			}
			
		}
	}
}
