package com.example.springai.config;

import jakarta.annotation.Nonnull;
import org.apache.commons.io.input.ReaderInputStream;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.InputStreamResource;
import org.springframework.util.PropertiesPersister;

import java.io.*;
import java.util.Properties;

public class YamlPropertiesLoader implements PropertiesPersister {

    @Override
    public void load(@Nonnull Properties props, @Nonnull InputStream is) throws IOException {
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new InputStreamResource(is));
        props.putAll(yaml.getObject());
    }

    @Override
    public void load(@Nonnull Properties props, @Nonnull Reader reader) throws IOException {
        InputStream is = ReaderInputStream.builder().setReader(reader).get();
        this.load(props, is);
    }

    @Override
    public void store(@Nonnull Properties props, @Nonnull OutputStream os, @Nonnull String header) throws IOException {
        throw new UnsupportedEncodingException("Storing is not supported by YamlPropertiesLoader");
    }

    @Override
    public void store(@Nonnull Properties props, @Nonnull Writer writer, @Nonnull String header) throws IOException {
        throw new UnsupportedEncodingException("Storing is not supported by YamlPropertiesLoader");
    }

    @Override
    public void loadFromXml(@Nonnull Properties props, @Nonnull InputStream is) throws IOException {
        throw new UnsupportedEncodingException("Loading from XML is not supported by YamlPropertiesLoader");
    }

    @Override
    public void storeToXml(@Nonnull Properties props, @Nonnull OutputStream os, @Nonnull String header) throws IOException {
        throw new UnsupportedEncodingException("Storing to XML is not supported by YamlPropertiesLoader");
    }

    @Override
    public void storeToXml(@Nonnull Properties props, @Nonnull OutputStream os, @Nonnull String header, String encoding) throws IOException {
        throw new UnsupportedEncodingException("Storing to XML is not supported by YamlPropertiesLoader");
    }

}
