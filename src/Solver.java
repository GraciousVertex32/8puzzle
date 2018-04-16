import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
public final class Solver
{
    private final boolean issolvable;
    private final int moves;
    private final Searchnode goal;
    public Solver(Board initial)
    {
        Searchnode currentmin,currentmin2;
        boolean solved = false;
        boolean tempissolvable = true; // have to give initial value
        int distributor = 0; // distribute twin and original operation
        MinPQ minpq = new MinPQ(manhattan()); // original queue
        Searchnode s0 = new Searchnode(initial,0,null);
        minpq.insert(s0);
        MinPQ minpq2 = new MinPQ(manhattan()); // twin queue
        Searchnode s1 = new Searchnode(initial.twin(),0,null);
        minpq2.insert(s1);
        while (!solved)
        {
            if (distributor == 0)
            {
                if (!((Searchnode) minpq.min()).board().isGoal())
                {
                    currentmin = (Searchnode) minpq.delMin();
                    for (Board board : currentmin.board().neighbors())
                    {
                        if (!board.equals(currentmin.getPredecessor().board()))
                        {
                            Searchnode sn = new Searchnode(board,currentmin.moves()+1,currentmin);
                            minpq.insert(sn);
                        }
                    }
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
                if (!((Searchnode) minpq2.min()).board().isGoal())
                {
                    currentmin2 = (Searchnode) minpq2.delMin();
                    for (Board board : currentmin2.board().neighbors())
                    {
                        if (!board.equals(currentmin2.getPredecessor().board()))
                        {
                            Searchnode sn2 = new Searchnode(board,currentmin2.moves()+1,currentmin2);
                            minpq2.insert(sn2);
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
        moves =((Searchnode)minpq.min()).moves();
        goal = (Searchnode) minpq.min();
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
        List<Board> solution = new LinkedList<Board>();
        Searchnode current = goal;
        for (int i=0;i<moves;i++)
        {
            solution.add(current.board());
            current = current.getPredecessor();
        }
        return solution;
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
    public final class Searchnode
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
        public int moves()
        {
            return moves;
        }
        public Board board()
        {
            return board;
        }
        public Searchnode getPredecessor()
        {
            return predecessor;
        }
    }
    private Comparator<Searchnode> manhattan()
    {
        return new NodeComparator();
    }
    private class NodeComparator implements Comparator<Searchnode>
    {
        public int compare(Searchnode o1,Searchnode o2)
        {
            if (o1.board().manhattan() > o2.board().manhattan())
            {
                return 1;
            }
            if (o1.board().manhattan() < o2.board().manhattan())
            {
                return -1;
            }
            return 0;
        }
    }
}
