import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
public final class Solver
{
    private final boolean issolvable;
    private final int moves;
    public Solver(Board initial)
    {
        int count = 0; // count moves need to solve
        boolean solved = false;
        boolean tempissolvable = true; // have to give initial value
        int distributor = 0; // distribute twin and original operation
        Searchnode previous; // link nodes
        Board currentmin;
        MinPQ minpq = new MinPQ(); // original queue
        minpq.insert(initial);
        Searchnode s0 = new Searchnode(initial,0,null);
        previous = s0;
        MinPQ minpq2 = new MinPQ(); // twin queue
        minpq2.insert(initial.twin());
        while (!solved)
        {
            if (distributor == 0)
            {
                if (!((Board) minpq.min()).isGoal())
                {
                    currentmin = (Board) minpq.delMin();
                    for (Board board : currentmin.neighbors())
                    {
                       Searchnode sn = new Searchnode(board,count,previous);

                       if (!board.equals(previous.board))
                       {
                           minpq.insert(board);
                       }
                       previous = sn;
                    }
                    count++;
                }
                else
                {
                    solved = true;
                    tempissolvable = true;
                }
                distributor = 1;
            }
            else
            {
                if (!((Board) minpq2.min()).isGoal())
                {
                    for (Board board : ((Board) minpq2.delMin()).neighbors())
                    {
                        if (!minpq2.min().equals(board))
                        {
                            minpq2.insert(board);
                        }
                    }
                }
                else
                {
                    solved = true;
                    tempissolvable = false;
                }
                distributor = 0;
            }
        }
        issolvable = tempissolvable;
        moves = count;
    }
    public boolean isSolvable()            // is the initial board solvable?
    {
        return issolvable;
    }
    public int moves()                     // min number of moves to solve initial board
    {
        if (!issolvable)
        {
            return -1;
        }
        return moves;
    }
    public Iterable<Board> solution()      // sequence of boards in a shortest solution; null if unsolvable
    {
        
    }
    public static void main(String[] args)
    {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        // solve the puzzle
        Solver solver = new Solver(initial);
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else
        {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
    private final class Searchnode
    {
        private final Board board;
        private final int moves;
        private final Searchnode predecessor;
        public Searchnode(Board current,int moves,Searchnode pre)
        {
            this.board = current;
            this.moves = moves;
            this.predecessor =pre;
        }
    }
}
