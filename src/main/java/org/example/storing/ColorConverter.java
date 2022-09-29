package org.example.storing;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import javafx.scene.paint.Color;

public class ColorConverter implements Converter {


    @Override
    public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext marshallingContext) {
        Color color = (Color) o;
        writer.startNode("red");
        writer.setValue(String.valueOf(color.getRed()));
        writer.endNode();
        writer.startNode("green");
        writer.setValue(String.valueOf(color.getGreen()));
        writer.endNode();
        writer.startNode("blue");
        writer.setValue(String.valueOf(color.getBlue()));
        writer.endNode();
        writer.startNode("opacity");
        writer.setValue(String.valueOf(color.getOpacity()));
        writer.endNode();
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader hierarchicalStreamReader, UnmarshallingContext unmarshallingContext) {
        return null;
    }

    @Override
    public boolean canConvert(Class aClass) {
        return aClass.equals(Color.class);
    }
}
