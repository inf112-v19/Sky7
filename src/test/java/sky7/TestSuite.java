package sky7;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import sky7.BoardTests.BoardGeneratorTest;
import sky7.BoardTests.BoardTests;
import sky7.Host.HostTest;
import sky7.client.ClientTest;

@RunWith(Suite.class)

@SuiteClasses({
    BoardGeneratorTest.class,
    BoardTests.class,
    ClientTest.class,
    FlagTest.class,
    ICellTest.class,
    ProgramDeckTest.class,
    HostTest.class
})

public class TestSuite {

}
