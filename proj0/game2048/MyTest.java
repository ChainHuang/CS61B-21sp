package game2048;
import org.junit.Test;
public class MyTest extends TestUtils{
    @Test
    public void testSingleColHelper() {
        int[][] before = new int[][] {
                {4, 0, 0, 0},
                {4, 0, 0, 0},
                {4, 0, 0, 0},
                {4, 0, 0, 0},
        };
        int[][] after = new int[][] {
                {8, 0, 0, 0},
                {8, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
        };//instead of 16 x x x, cause a tile that is the result of a merge will not merge again on that tile.

        model = new Model(before, 0, 0, false);
        String prevBoard = model.toString();
        boolean changed = model.singleColHelper(0);
        checkChanged(Side.NORTH, true, changed);
        checkModel(after, 0, 0, prevBoard, Side.NORTH);
    }
}
