package com.urise.webapp.storage;

//в этом классе сгруперруем все тесты для их удобного запуска
//с пом средств JUnit(анотаций)

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses(
        {
                ArrayStorageTest.class,
                SortedArrayStorageTest.class,
                ListStorageTest.class,
                MapUuidStorageTest.class
        }
)
public class AllStorageTest {
}
