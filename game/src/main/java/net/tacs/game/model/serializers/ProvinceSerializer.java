package net.tacs.game.model.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import net.tacs.game.model.Municipality;
import net.tacs.game.model.Province;

import java.io.IOException;
import java.util.Map;

public class ProvinceSerializer extends StdSerializer<Province> {

    public ProvinceSerializer(){
        this(null);
    }

    protected ProvinceSerializer(Class<Province> t) {
        super(t);
    }

    @Override
    public void serialize(Province province, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("id", province.getId());
        jsonGenerator.writeStringField("nombre", province.getNombre());
        jsonGenerator.writeObjectField("centroide", province.getCentroide());
        jsonGenerator.writeArrayFieldStart("municipalities");
        //jsonGenerator.writeStartArray();
        for (Map.Entry<Integer, Municipality> aMuni : province.getMunicipalities().entrySet()) {
            jsonGenerator.writeObject(aMuni.getValue());
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();
    }
}
