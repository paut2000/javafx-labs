package org.example.storing;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import javafx.scene.paint.Color;

import java.io.IOException;

public class ColorDeserializer extends StdDeserializer<Color> {

    public ColorDeserializer() {
        this(null);
    }

    public ColorDeserializer(Class vc) {
        super(vc);
    }

    @Override
    public Color deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        return new Color(
                node.get("red").asDouble(),
                node.get("green").asDouble(),
                node.get("blue").asDouble(),
                node.get("opacity").asDouble()
        );
    }
}
