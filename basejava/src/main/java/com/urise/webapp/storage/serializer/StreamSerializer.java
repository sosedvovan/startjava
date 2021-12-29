package com.urise.webapp.storage.serializer;

import com.urise.webapp.model.Resume;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
//применяем паттерн стратегия для сериализаций разными способами
//это интерфеис, реализации которого и есть вариации сериализаций разными способами

public interface StreamSerializer {

    void doWrite(Resume r, OutputStream os) throws IOException;

    Resume doRead(InputStream is) throws IOException;
}