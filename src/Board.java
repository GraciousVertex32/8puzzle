import java.util.List;
import java.util.LinkedList;
import edu.princeton.cs.algs4.StdRandom;
public final class Board // class must be immutable
{
    private final int dimension;
    private final int[][] layout;
    private final int hamming;
    private final int manhattan;
    private final boolean goal;
    private final int[] blankblock = new int[2];
    //private final int predecessorblank;// 0 for no predecessor,1,2,3,4 mean predecessor blank block is at the up,down,left,right of current blank block
    //probably need remove
    public Board(int[][] blocks)
    {
        int temphamming = 0; //local variable for hamming
        int a; //local variable refer to number in current position
        int tempmanhattan = 0; //local variable for manhattan
        int[] tempblankblock = new int[2];// local variable for blank position
        dimension = (int) blocks.length;
        layout = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++)
        {
            for (int j = 0; j < dimension; j++)
            {
                layout[i][j] = blocks[i][j];
                a = layout[i][j];
                if (layout[i][j] == 0) //get blankblock position
                {
                    tempblankblock[0] = i;
                    tempblankblock[1] = j;
                }
                if (i == dimension-1 && j == dimension -1 && layout[dimension-1][dimension-1] == 0) //the final number is right if it's 0
                {
                    break;
                }
                if (dimension * i + j + 1 != layout[i][j] && a != 0) //get hamming
                {
                    temphamming++;
                }
                if (a != 0)
                {
                    tempmanhattan = tempmanhattan + Math.abs((a - 1) / dimension - i) + Math.abs((a - 1) % dimension - j); //get manhattan
                }
            }
        }
        if (temphamming == 0)
        {
            goal = true;
        }
        else
        {
            goal = false;
        }
        hamming = temphamming;
        manhattan = tempmanhattan;
        blankblock[0] = tempblankblock[0];
        blankblock[1] = tempblankblock[1];
    }
    public int dimension() // board dimension n
    {
        return dimension;
    }
    public int hamming() // number of blocks out of place
    {
        return hamming;
    }
    public int manhattan() // sum of Manhattan distances between blocks and goal
    {
        return manhattan;
    }
    public boolean isGoal() // is this board the goal board?
    {
        return goal;
    }
    public Board twin() // a board that is obtained by exchanging any pair of blocks
    {
        int x = 0, y = 0, a = 0, b = 0; // for pair indexes
        int tempstore; // for exchange
        for (int i = 0; i < dimension; i++) // get first block
        {
            for (int j = 0; j < dimension; j++)
            {
                if (layout[i][j] != 0)
                {
                    x = i;
                    y = j;
                    break;
                }
            }
        }
        for (int i = x; i < dimension; i++) // get second block
        {
            for (int j = y; j < dimension; j++)
            {
                if (layout[i][j] != 0)
                {
                    a = i;
                    b = j;
                    break;
                }
            }
        }
        return new Board(exchange(x, y, a, b, layout));
    }
    public Iterable<Board> neighbors()
    {
        List<Board> neighbors = new LinkedList<Board>();
        int x = blankblock[0];
        int y = blankblock[1];
        if (x - 1 >= 0)
        {
            Board board = new Board(exchange(x - 1, y, x, y, layout));
            neighbors.add(board);
        }
        if (x + 1 < dimension)
        {
            Board board = new Board(exchange(x + 1, y, x, y, layout));
            neighbors.add(board);
        }
        if (y - 1 >= 0)
        {
            Board board = new Board(exchange(x, y - 1, x, y, layout));
            neighbors.add(board);
        }
        if (y + 1 < dimension)
        {
            Board board = new Board(exchange(x, y + 1, x, y, layout));
            neighbors.add(board);
        }
        return neighbors;
    }
    public boolean equals(Object y)        // does this board equal y?
    {
        if (y instanceof Board)
        {
            for (int i = 0; i < dimension; i++) // get second block
            {
                for (int j = 0; j < dimension; j++)
                {
                    if (layout[i][j] != ((Board) y).layout[i][j])
                    {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }
    public String toString()               // string representation of this board
    {
        StringBuffer s = new StringBuffer();
        s.append(dimension+"/r/n");
        for (int i = 0; i < dimension; i++) // get second block
        {
            for (int j = 0; j < dimension; j++)
            {
                s.append(" "+layout[i][j]);
            }
            s.append("/r/n");
        }
        return s.toString();
    }
    private int[][] exchange(int x, int y, int a, int b, int[][] original) // return new pair changed array
    {
        int[][] changed = new int[original.length][original[0].length];
        for (int i = 0; i < original.length; i++)
        {
            for (int j = 0; j < original[0].length; j++)
            {
                changed[i][j] = original[i][j];
            }
        }
        int tempstore;
        tempstore = changed[x][y];
        changed[x][y] = changed[a][b];
        changed[a][b] = tempstore;
        return changed;
    }
}
