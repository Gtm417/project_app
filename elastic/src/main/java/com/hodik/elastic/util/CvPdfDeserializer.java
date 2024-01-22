package com.hodik.elastic.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class CvPdfDeserializer extends StdDeserializer<String> {
    protected CvPdfDeserializer() {
        super(String.class);
    }

    public CvPdfDeserializer(StdDeserializer<String> src) {
        super(src);
    }

    @Override
    public String deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException {
        byte[] bytesCV = parser.getBinaryValue();
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytesCV);

             PDDocument document = PDDocument.load(inputStream)) {

            PDFTextStripper textStripper = new PDFTextStripper();
            return textStripper.getText(document);
        }
    }
}