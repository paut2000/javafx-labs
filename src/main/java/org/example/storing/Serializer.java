package org.example.storing;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import org.example.App;
import org.example.model.element.AbstractElement;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class Serializer {

    private static final String PATH_TO_XML_FILE = "file.xml";
    private static final String PATH_TO_BINARY_FILE = "file.bin";
    private static final String PATH_TO_TEXT_FILE = "file.txt";

    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    private final StorableFactory factory = new StorableFactory();
    XStream xStream = new XStream();

    public Serializer() {
        xStream.addPermission(AnyTypePermission.ANY);
        xStream.registerConverter(new ColorConverter());
        xStream.autodetectAnnotations(true);
    }

    public void serializeToTextFile(List<? extends Storable> list) {
        try {
            FileWriter writer = new FileWriter(PATH_TO_TEXT_FILE);

            for (Storable element : list) {
                writer.write(element.serialize());
            }

            writer.close();
        } catch (IOException e) {
            LOGGER.info("Can't open file");
        }
    }

    public List<? extends Storable> deserializeFromTextFile() {
        List<Storable> list = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(PATH_TO_TEXT_FILE);
            Scanner objectScanner = new Scanner(fileReader);
            objectScanner.useDelimiter(";");

            while (objectScanner.hasNext()) {
                Scanner fieldScanner = new Scanner(objectScanner.next());
                fieldScanner.useDelimiter(",");
                list.add(factory.create(fieldScanner));
            }

            fileReader.close();
        } catch (IOException e) {
            LOGGER.info(e.getMessage());
        }
        return list;
    }

    public void serializeToBinaryFile(List<AbstractElement> list) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(PATH_TO_BINARY_FILE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            for (AbstractElement element : list) {
                objectOutputStream.writeObject(element);
                element.serializeColor(objectOutputStream);
            }
            objectOutputStream.flush();

            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            LOGGER.info(e.getMessage());
        }
    }

    public List<AbstractElement> deserializeFromBinaryFile() {
        List<AbstractElement> list = new ArrayList<>();
        try {
            FileInputStream fileInputStream = new FileInputStream(PATH_TO_BINARY_FILE);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            while (fileInputStream.available() > 0) {
                AbstractElement element = (AbstractElement) objectInputStream.readObject();
                element.deserializeColor(objectInputStream);
                list.add(element);
            }

            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.info(e.getMessage());
        }
        return list;
    }

    public void serializeToXmlFile(List<AbstractElement> list) {
        try {
            FileWriter writer = new FileWriter(PATH_TO_XML_FILE);
            xStream.toXML(list, writer);
            writer.close();
        } catch (IOException e) {
            LOGGER.info(e.getMessage());
        }
    }

    public List<AbstractElement> deserializeFromXmlFile() {
        Object o = xStream.fromXML(new File(PATH_TO_XML_FILE));
        if (o == null) return new ArrayList<>();
        return (List<AbstractElement>) o;
    }

}
