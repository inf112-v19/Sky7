package sky7;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import sky7.board.cellContents.Inactive.Flag;

import static org.junit.Assert.fail;

public class FlagTest {
    @Rule
    public ExpectedException thrown= ExpectedException.none();

    @Test
    public void flagNrTest(){
        thrown.expect(NullPointerException.class);  // If you try to load a Texture without LibGdx you get a null pointer exception
                                                    // This is okey for the test, so we ignore.
        for (int i = -10; i < 10; i++) {
            Flag flag = new Flag(i);
            try{
                flag.getTexture();
            } catch (IllegalArgumentException e){
                if(i >= 1 && i <= 4){
                    fail("got excpetion when you were not supposed to");
                }
            }
        }

    }
}
