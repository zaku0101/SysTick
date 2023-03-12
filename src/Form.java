import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Hashtable;

public class Form implements ActionListener, ChangeListener {
    private JPanel mainPanel;
    private JPanel RegistersPanel;
    private JPanel infoPanel;
    private JPanel valueSettingsPanel;
    private JPanel generatorPanel;
    private JLabel rvrLabel;
    private JLabel rvrValueLabel;
    private JLabel cvrLabel;
    private JLabel cvrValueLabel;
    private JComboBox sourceComboBox;
    private JLabel sourceLabel;
    private JButton enableButton;
    private JLabel interruptLabel;
    private JButton ONButton;
    private JCheckBox isInterruptCheckBox;
    private JCheckBox countFlagCheckBox;
    private JSlider modeSlider;
    private JButton generateButton;
    private JButton tickButton;
    private JLabel setRvrLabel;
    private JSpinner rvrSpinner;
    private JLabel burstLabel;
    private JLabel programNameLabel;
    private JButton linkButton;
    private JSlider rvrSlider;
    private JSlider burstSlider;
    private JSlider genModeSlider;
    private JLabel burstValueLabel;
    private JLabel delayLabel;
    private JLabel delayValueLabel;
    private JSlider delaySlider;

    SysTick systick =new SysTick();
    Generator generator=new Generator();
    final URI uri;

    {
        try {
            uri = new URI("https://www.youtube.com/watch?v=G1IbRujko-A&t=24s");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    Form()
    {
        generateButton.setEnabled(false);
        generator.setMode(PulseSource.CONTINOUS_MODE);
        systick.setSourceInternal();
        generator.start();
        JFrame frame = new JFrame();

        enableButton.addActionListener(this);
        ONButton.addActionListener(this);
        tickButton.addActionListener(this);
        rvrSlider.addChangeListener(this);
        sourceComboBox.addActionListener(this);
        linkButton.addActionListener(this);

        rvrSlider.setMinimum(0);
        rvrSlider.setMaximum(100);
        rvrSlider.setMajorTickSpacing(20);
        rvrSlider.setMinorTickSpacing(5);
        rvrSlider.setPaintTicks(true);
        rvrSlider.setPaintTrack(true);
        rvrSlider.setSnapToTicks(true);

        isInterruptCheckBox.setEnabled(false);

        countFlagCheckBox.setEnabled(false);

        burstSlider.setMaximum(0);
        burstSlider.setMaximum(100);
        burstSlider.setMajorTickSpacing(20);
        burstSlider.setMinorTickSpacing(5);
        generateButton.addActionListener(this);

        delaySlider.setMaximum(1000);
        delaySlider.setMinimum(0);
        delaySlider.setMinorTickSpacing(100);
        delaySlider.addChangeListener(this);
        delaySlider.setPaintTicks(true);
        delaySlider.setPaintTrack(true);
        delaySlider.setSnapToTicks(true);
        delaySlider.setMajorTickSpacing(100);


        modeSlider.addChangeListener(this);
        Hashtable<Integer,JLabel> Labels=new Hashtable<>();
        Labels.put(0,new JLabel("OFF"));
        Labels.put(1,new JLabel("ON"));
        modeSlider.setLabelTable(Labels);

        genModeSlider.addChangeListener(this);
        Hashtable<Integer,JLabel> Labels1=new Hashtable<>();
        Labels1.put(0,new JLabel("CONT"));
        Labels1.put(1,new JLabel("BURST"));
        genModeSlider.setLabelTable(Labels1);

        generator.addActionListener(this);

        burstSlider.addChangeListener(this);

        frame.setLayout(null);
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(1500,700);

    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==enableButton)
        {

            if(systick.getEnabled())
            {
                systick.setDisable();
                System.out.println(systick.getEnabled());
                enableButton.setText("Enable");
            }else
            {
                systick.setEnable();
                System.out.println(systick.getEnabled());
                enableButton.setText("Disable");
            }

        }
        if(e.getSource()==ONButton)
        {
            if(systick.isInterruptFlag())
            {
                systick.setInterruptDisable();
                System.out.println(systick.isInterruptFlag());
                ONButton.setText("ON");
            }else
            {
                systick.setInterruptEnable();
                System.out.println(systick.isInterruptFlag());
                ONButton.setText("OFF");
            }
        }
        if(e.getSource()==tickButton)
        {
            if(systick.getSource()&&sourceComboBox.getSelectedIndex()==1)
            {

                systick.tickExternal();
                System.out.println(systick.getCVR());
                countFlagCheckBox.setSelected(systick.getCountFlag());
                cvrValueLabel.setText(String.valueOf(systick.getCVR()));

                if(systick.getCVR()==0)
                isInterruptCheckBox.setSelected(systick.isInterrupt());


                if(systick.getCVR()==0)
                {
                countFlagCheckBox.setSelected(true);
                }
                else{
                    countFlagCheckBox.setSelected(false);
                }
            }

        if(e.getSource()==sourceComboBox) {
            if (sourceComboBox.getSelectedIndex() == 0) {
                systick.setSourceInternal();
            } else if (sourceComboBox.getSelectedIndex() == 1) {
                systick.setSourceExternal();
            }
        }
        }
        if(e.getSource()==linkButton) {
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.browse(uri);
                } catch (Exception ex) {

                }
            }
        }
        if(e.getSource()==generateButton)
        {
            if(generator.on)
            {
                generator.stopGeneration();
                System.out.println(generator.on);
                generateButton.setText("Generate");



            }else
            {
                generator.setPulseCount(burstSlider.getValue());
                generator.burst=burstSlider.getValue();
                generator.startGeneration();
                System.out.println(generator.on);
                generateButton.setText("Off");


            }
        }
        if(e.getSource()==generator)
        {
            if(generator.getMode()==0)
            {

            if(generator.getPulseCount()>0)
            {

                 systick.tickInternal();


                if(systick.getCVR()==0)
                {
                    countFlagCheckBox.setSelected(true);
                    isInterruptCheckBox.setSelected(systick.isInterrupt());
                }
                else{
                    countFlagCheckBox.setSelected(false);
                }

            }
            else{
                generateButton.setText("Generate");
            }
            } else if (generator.getMode()==1) {
            systick.tickInternal();




                if(systick.getCVR()==0)
                {
                    countFlagCheckBox.setSelected(true);
                    isInterruptCheckBox.setSelected(systick.isInterrupt());
                }
                else{
                    countFlagCheckBox.setSelected(false);
                }
                if(generator.getPulseCount()<=0) generateButton.setText("Generate");
            }


            cvrValueLabel.setText(String.valueOf(systick.getCVR()));
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if(e.getSource()==rvrSlider)
        {
            systick.setRVR(rvrSlider.getValue());
            rvrValueLabel.setText(String.valueOf(rvrSlider.getValue()));
            System.out.println(systick.getRVR());
        }
        if(e.getSource()==modeSlider)
        {
           if(modeSlider.getValue()==0)
           {
               generateButton.setEnabled(false);

           }else if(modeSlider.getValue()==1)
               generateButton.setEnabled(true);


        }
        if(e.getSource()==genModeSlider)
        {
         if(genModeSlider.getValue()==0)
         {
             generator.setMode(PulseSource.CONTINOUS_MODE);
         } else if (genModeSlider.getValue()==1) {
             System.out.println("1");
             generator.setMode(PulseSource.BURST_MODE);
         }

        }
        if(e.getSource()==burstSlider)
        {
            burstValueLabel.setText(String.valueOf(burstSlider.getValue()));
            generator.burst=burstSlider.getValue();
            generator.pulseCount=burstSlider.getValue();



        }
        if(e.getSource()==delaySlider)
        {
            delayValueLabel.setText(String.valueOf(delaySlider.getValue()));
            generator.pulseDelay=delaySlider.getValue();
        }
    }


}
