package pictire;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import picture.Numbers;
import picture.NumbersSide;
import picture.StashedPicture;

class NumbersTest {
    @Test
    void testSimple() {
        boolean[][] field = {
                {false},
                {true},
                {true},
                {true},
                {false}
        };

        Numbers numbers = new Numbers(new StashedPicture(field), NumbersSide.TOP);

        int size = numbers.getSize();
        Assertions.assertEquals(1, size);
        Assertions.assertEquals(3, numbers.getVector(0)[0]);
    }

    @Test
    void testEdge() {
        boolean[][] field = {
                {false},
                {true},
                {true},
                {true},
                {true}
        };

        Numbers numbers = new Numbers(new StashedPicture(field), NumbersSide.TOP);

        int size = numbers.getSize();
        Assertions.assertEquals(1, size);
        Assertions.assertEquals(4, numbers.getVector(0)[0]);
    }

    @Test
    void testTwoInColumn() {
        boolean[][] field = {
                {true},
                {false},
                {true},
                {true},
                {true}
        };

        Numbers numbers = new Numbers(new StashedPicture(field), NumbersSide.TOP);

        int size = numbers.getSize();
        Assertions.assertEquals(1, size);
        Assertions.assertEquals(1, numbers.getVector(0)[0]);
        Assertions.assertEquals(3, numbers.getVector(0)[1]);
    }

    @Test
    void testLeftAndTop() {
        boolean[][] fieldValues = {
                {true, true, false},
                {false, true, true},
                {true, false, false},
                {true, false, true},
        };

        int[][] topNumbers = {
                {1, 2},
                {2},
                {1, 1}
        };

        int[][] leftNumbers = {
                {2},
                {2},
                {1},
                {1, 1}
        };

        StashedPicture guessedPicture = new StashedPicture(fieldValues);

        Numbers numbers = new Numbers(guessedPicture, NumbersSide.TOP);
        assertEqualsNumbers(topNumbers, numbers);

        numbers = new Numbers(guessedPicture, NumbersSide.LEFT);
        assertEqualsNumbers(leftNumbers, numbers);
    }

    private void assertEqualsNumbers(int[][] expected, Numbers actual) {
        int size = actual.getSize();
        Assertions.assertEquals(expected.length, size);

        for (int index = 0; index < size; index++) {
            int[] vector = actual.getVector(index);
            Assertions.assertEquals(expected[index].length, vector.length);

            for (int depth = 0; depth < vector.length; depth++) {
                Assertions.assertEquals(expected[index][depth], vector[depth]);
            }
        }
    }
}
