package meleh.demo;

import meleh.devices.Device;
import meleh.xml.XMLReader;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main (String[] args) {

        try {
            ArrayList<Device> arrayList = new ArrayList<>();
            String command = "", file = "";

            Scanner scanner = new Scanner(System.in);
            do {
                command = scanner.next();
                switch (command) {
                    case "read":
                        arrayList = XMLReader.readXML("D:/Projects/University/UP_4sem/Lab2/src/input.xml");
                        break;
                    case "calculate":
                        System.out.println("Потребляемая энергия: " + arrayList.stream().filter(x->x.isTurnedOn()).mapToInt(x->x.getPowerUsage()).sum() + "\n");
                        break;
                    case "sort":
                        arrayList.stream().sorted((o1, o2) -> o1.getPowerUsage() - o2.getPowerUsage()).forEach(x -> System.out.println(x));
                        break;
                    case "find":
                        System.out.println("Показать включенные/выключенные приборы (1/2)\n");
                        int type = scanner.nextInt();
                        if (type == 1) {
                            arrayList.stream().filter(x->x.isTurnedOn()).forEach(x -> System.out.println(x));
                        }
                        if (type == 2) {
                            arrayList.stream().filter(x->!x.isTurnedOn()).forEach(x -> System.out.println(x));
                        }
                        break;
                    case "help":
                        System.out.println("read - считать meleh.xml файл");
                        System.out.println("calculate - подсчитать потребляемую мощность");
                        System.out.println("sort - провести сортировку приборов в квартире на основе мощности");
                        System.out.println("find - найти прибор в квартире, соответствующий заданному диапазону параметров");
                        break;
                    default: {
                        System.out.println("No such command!\n");
                        break;
                    }
                }
            }
            while (!command.equals("exit"));
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
