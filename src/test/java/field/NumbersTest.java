package field;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        Numbers numbers = new Numbers(new Field(field), NumbersSide.Top);

        int size = numbers.getSize();
        assertEquals(1, size);
        assertEquals(3, numbers.getVector(0)[0]);
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

        Numbers numbers = new Numbers(new Field(field), NumbersSide.Top);

        int size = numbers.getSize();
        assertEquals(1, size);
        assertEquals(4, numbers.getVector(0)[0]);
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

        Numbers numbers = new Numbers(new Field(field), NumbersSide.Top);

        int size = numbers.getSize();
        assertEquals(1, size);
        assertEquals(1, numbers.getVector(0)[0]);
        assertEquals(3, numbers.getVector(0)[1]);
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

        Field field = new Field(fieldValues);

        Numbers numbers = new Numbers(field, NumbersSide.Top);
        assertEqualsNumbers(topNumbers, numbers);

        numbers = new Numbers(field, NumbersSide.Left);
        assertEqualsNumbers(leftNumbers, numbers);
    }

    private void assertEqualsNumbers(int[][] expected, Numbers actual) {
        int size = actual.getSize();
        assertEquals(expected.length, size);

        for (int index = 0; index < size; index++) {
            int[] vector = actual.getVector(index);
            assertEquals(expected[index].length, vector.length);

            for (int depth = 0; depth < vector.length; depth++) {
                assertEquals(expected[index][depth], vector[depth]);
            }
        }
    }
}
