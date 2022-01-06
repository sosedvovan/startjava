package com.urise.webapp.storage;

import com.urise.webapp.Config;
import com.urise.webapp.storage.serializer.XmlStreamSerializer;

public class SqlStorageTest extends AbstractStorageTest  {

    public SqlStorageTest() {
        //в super отправляем объект SqlStorage несущий в себе url, логин и пароль от дб
        super(Config.get().getStorage());
    }

}

