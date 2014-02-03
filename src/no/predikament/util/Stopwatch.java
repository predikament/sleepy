package no.predikament.util;


public class Stopwatch 
{
    private long startTime = 0;
    private long stopTime = 0;
    private boolean running;

    public Stopwatch()
    {
    	this(false);
    }
    
    public Stopwatch(final boolean running)
    {
    	this.startTime = 0;
    	this.stopTime = 0;
    	
    	if (running) start();
    }
    
    public void start() 
    {
        this.startTime = System.currentTimeMillis();
        this.running = true;
    }
    
    public void stop() 
    {
        this.stopTime = System.currentTimeMillis();
        this.running = false;
    }
    
    public void reset()
    {
    	stop();
    	start();
    }
  
    // Elapsed time in milliseconds
    public long getElapsedTime() 
    {
        if (running) return (System.currentTimeMillis() - startTime);
        
        return (stopTime - startTime);
    }
    
    // Elapsed time in seconds
    public long getElapsedTimeSecs() 
    {
        if (running) return ((System.currentTimeMillis() - startTime) / 1000);
        
        return  ((stopTime - startTime) / 1000);
    }
    
    public final boolean isRunning()
    {
    	return running;
    }
}