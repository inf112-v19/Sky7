package sky7;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import sky7.BoardTests.BoardTests;
import sky7.BoardTests.DrawPriorityTest;
import sky7.client.ClientTest;

@RunWith(Suite.class)

@SuiteClasses({
    BoardTests.class,
    RoboTest.class,
    ClientTest.class,
    FlagTest.class,
    ICellTest.class,
    ProgramDeckTest.class,
    DrawPriorityTest.class
})

public class TestSuite {

}
