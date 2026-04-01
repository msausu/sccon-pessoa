package com.sccon.geo.pessoa.infrastructure.jackson;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.SerializationContext;

public class BigDecimalPtBrSerializer extends ValueSerializer<BigDecimal> {   // ← Changed

    private static final Locale LOCALE = new Locale("pt", "BR");

    @Override
    public void serialize(BigDecimal value, JsonGenerator gen, SerializationContext context) {   // ← SerializationContext instead of SerializerProvider

        if (value == null) {
            gen.writeNull();
            return;
        }

        NumberFormat format = NumberFormat.getNumberInstance(LOCALE);
        gen.writeString(format.format(value));
    }
}