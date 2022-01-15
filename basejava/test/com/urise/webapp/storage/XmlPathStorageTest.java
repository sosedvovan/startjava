package com.urise.webapp.storage;

import com.urise.webapp.storage.serializer.XmlStreamSerializer;

public class XmlPathStorageTest extends AbstractStorageTest  {

    public XmlPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new XmlStreamSerializer()));
        //super(new PathStorage("C:\\Users\\Vladimir\\IdeaProjects\\startjava\\basejava\\storage", new XmlStreamSerializer()));
    }

}

