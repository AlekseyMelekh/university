import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static jdk.nashorn.internal.objects.NativeMath.min;

public class Window extends JFrame {

    private JTabbedPane tabbedPane;
    private ClockPanel panelTask1;
    private ImagePanel panelTask2;
    private JPanel panelTask3;
    private Timer timer;
    private TimerTask clock;
    private TimerTask runningImage;
    private JSlider slider;
    private JButton changeDirection;
    private double speed = 0;

    static class ParsedData {
        String name;
        double value;
    }

    public Window () {
        this.setBounds(700, 200, 600, 700);
        this.setTitle("Lab4");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panelTask1 = new ClockPanel();
        panelTask2 = new ImagePanel();
        panelTask3 = new JPanel();

        timer = new Timer();
        clock = new TimerTask() {
            @Override
            public void run() {
                panelTask1.addAngle(2 * Math.PI / 60000);
                panelTask1.repaint();
            }
        };
        timer.schedule(clock, 100, 1);

        slider = new JSlider(0, 100, 0);
        changeDirection = new JButton("change direction");
        panelTask2.setLayout(new BorderLayout());
        panelTask2.add(slider, BorderLayout.SOUTH);
        panelTask2.add(changeDirection, BorderLayout.NORTH);

        changeDirection.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelTask2.changeDirection();
            }
        });

        runningImage = new TimerTask() {
            @Override
            public void run() {
                panelTask2.addAngle(min(panelTask2.getWidth(), panelTask2.getHeight()) * speed * Math.PI / 10000000);
                panelTask2.repaint();
            }
        };
        timer.schedule(runningImage, 100, 1);

        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                speed = slider.getValue();
            }
        });


        JButton jsonOpen = new JButton("Open");
        DefaultPieDataset pieDataset = new DefaultPieDataset();
        jsonOpen.addActionListener(e -> {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            JsonReader reader = null;
            try {
                reader = new JsonReader(new FileReader("D:\\Projects\\University\\UP_4sem\\Lab4\\src\\input.json"));
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            ArrayList<ParsedData> jsonParsed = gson.fromJson(reader, new TypeToken<ArrayList<ParsedData>>() {}.getType());
            pieDataset.clear();
            for (ParsedData parsedData : jsonParsed) {
                pieDataset.setValue(parsedData.name, parsedData.value);
            }
        });
        JFreeChart chart = ChartFactory.createPieChart("Chart", pieDataset, true, true, false);
        PieSectionLabelGenerator generator = new StandardPieSectionLabelGenerator("{0}: {1} ({2})", new DecimalFormat("0"), new DecimalFormat("0%"));
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setLabelGenerator(generator);
        ChartPanel pane = new ChartPanel(chart);
        panelTask3.setLayout(new BorderLayout());
        panelTask3.add(pane, BorderLayout.CENTER);
        panelTask3.add(jsonOpen, BorderLayout.SOUTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.add("Task 1", panelTask1);
        tabbedPane.add("Task 2", panelTask2);
        tabbedPane.add("Task 3", panelTask3);

        Container container = getContentPane();
        container.add(tabbedPane);

    }

}
