package com.urise.webapp.storage;

//в этом классе сгруперруем все тесты для их удобного запуска
//с пом средств JUnit(анотаций)

//Todo: Из-за того, что количество тестовых классов растет, воспользуйтесь аннотациями JUnit,
// которые помогут упростить их запуск (только для JUnit4):

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses(
        {
                ArrayStorageTest.class,
                SortedArrayStorageTest.class,
                ListStorageTest.class,
                MapUuidStorageTest.class,
                MapResumeStorageTest.class,
                ObjectFileStorageTest.class,
                ObjectPathStorageTest.class,
                XmlPathStorageTest.class,
                JsonPathStorageTest.class
        }
)
public class AllStorageTest {
}
