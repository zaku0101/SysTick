import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Generator extends Thread implements PulseSource {
    ActionListener listener;

    boolean alive;
    boolean on;

    byte mode=PulseSource.BURST_MODE;
    int pulseCount,pulseDelay;
    int burst;

    public void addActionListener(ActionListener l)
    {
       listener= AWTEventMulticaster.add(listener,l);

    }
    @Override
    public void removeActionListener(ActionListener pl) {
        AWTEventMulticaster.remove(listener,pl);
    }
    public void run()
    {
        alive=true;
        while (alive) {
            if (on) {
                if (mode == BURST_MODE) {
                pulseCount--;
                if(pulseCount<=0) {
                    on=false;
                }
                }
                    try {
                        Thread.sleep(pulseDelay);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    if (listener == null) {
                        System.out.println("Tick");
                    } else {
                        listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "tick"));
                    }

                }


        else
            {
                try
                {
                    Thread.sleep(1);
                } catch (InterruptedException e)
                {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    public void killThread()
    {
        alive=false;
    }

    public boolean checkOn()
    {
        return on;
    }
    public static void main(String[] args) {


    }





    @Override
    public void startGeneration() {
        on=true;
    }

    @Override
    public void setMode(byte mode) {
    this.mode=mode;
    }

    @Override
    public byte getMode() {
        return mode;
    }

    @Override
    public void stopGeneration() {
    on=false;
    }

    @Override
    public void setPulseDelay(int ms) {
        pulseDelay=ms;

    }

    @Override
    public int getPulseDelay() {
        return pulseDelay;
    }

    @Override
    public void setPulseCount(int burst) {
        pulseCount=burst;

    }
    public int getPulseCount() {
        return pulseCount;

    }
}
