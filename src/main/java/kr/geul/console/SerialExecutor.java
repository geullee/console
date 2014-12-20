package kr.geul.console;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Executor;

public class SerialExecutor implements Executor {

	final Queue<Runnable> tasks = new ArrayDeque<Runnable>();
	final Executor executor;
	Runnable active;

	public SerialExecutor(Executor executor) {	   
		this.executor = executor;    
	}

	public synchronized void execute(final Runnable r, final String line) {
		
		if (r != null) {
		
			tasks.offer(new Runnable() {

				public void run() {
					
					try {	    	
						
						if (line.length() > 0) {
							
							if (line.charAt(line.length() - 1) == ';')
								System.out.println(line.substring(0, line.length() - 1));
							else
								System.out.println(line.substring(0, line.length()));
						}
							
						r.run();
					}

					finally {
						scheduleNext();	           
					}

				}

			});

			if (active == null) 	    	 
				scheduleNext();
			
		}

	}

	protected synchronized void scheduleNext() {

		if ((active = tasks.poll()) != null) {  
			executor.execute(active);	       
		}
		
	}

	public void execute(final Runnable command) {
		execute(command, "");
	}

}
