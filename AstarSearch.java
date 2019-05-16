import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

/*
// A* Search Algorithm

1.  Initialize the open list

2.  Initialize the closed list
    put the starting node on the open
    list (you can leave its f at zero)

3.  while the open list is not empty

    a) find the node with the least f on
       the open list, call it "q"

    b) pop q off the open list

    c) generate q's 8 successors and (set their parents to q = no!!! - it goes infinite)

    d) for each successor

        i) if successor is the goal, stop search
          successor.g = q.g + distance between successor and q
          successor.h = distance from goal to successor

          successor.f = successor.g + successor.h

        **
            if a new shorter path to this neighbor is found - we do something
                if it is in closed, remove it
                if it is in not in open, add it to open

            otherwise, we ignore only if it is in OPEN already


            if ( Neighbor is not in OPEN || neighbor's F > NewF )

               update F, G, H value and set parent = currentNode

               if(it is in OPEN){
                  // do nothing. we already updated
               }

               if(it is not in OPEN){
                  update F, G, H value and set parent = currentNode
                  add it into open
               }

               if(it is in closed){
                  if(it has less F then new F) ignore
                  else
                     remove it from close.
                     add it into open
               }

               if(it is not in closed){
                  ignore
               }
            }

            // if neighbor is not in closed list
            //         if neighbor's G < current G
            //            currentG = neighbor G
            //            currentG.parent = neighbor parent


     end (for loop)

    e) push q on the closed list
    end (while loop)

 */
public class AstarSearch {

    static int[][] grid = new int[][]{
                                { 1, 0, 1, 1, 1, 1, 0, 1, 1, 1},
                                { 1, 1, 1, 0, 1, 1, 1, 0, 1, 1},
                                { 1, 1, 1, 0, 1, 1, 0, 1, 0, 1},
                                { 0, 0, 1, 0, 1, 0, 0, 0, 0, 1},
                                { 1, 1, 1, 0, 1, 1, 1, 0, 1, 0},
                                { 1, 0, 1, 1, 1, 1, 0, 1, 0, 0},
                                { 1, 0, 0, 0, 0, 1, 0, 0, 0, 1},
                                { 1, 0, 1, 1, 1, 1, 0, 1, 1, 1},
                                { 1, 1, 1, 0, 0, 0, 1, 0, 0, 1}
                            };
    static int[] source = new int[]{0, 0};
    static int[] dest = new int[]{9, 9};

//    static int[][] grid_small = new int[][]{
//                                { 1, 1, 1, 1, 1},
//                                { 1, 1, 1, 0, 1},
//                                { 1, 1, 1, 0, 1},
//                                { 0, 0, 1, 0, 1},
//                                { 1, 1, 1, 1, 1}
//                            };

    static int[][] grid_small = new int[][]{
                                { 1, 5, 1, 1, 1},
                                { 1, 8, 3, 2, 1},
                                { 0, 0, 0, 3, 1},
                                { 0, 0, 0, 4, 1},
                                { 1, 1, 0, 5, 1}
                            };

    static int[] dest_small = new int[]{4, 4};

    public static void main(String... args){

        AstarSearch_Algorithm ctrl = new AstarSearch_Algorithm(grid_small.length, grid_small[0].length, grid_small);

        System.out.println(ctrl.search(source, dest_small));
    }

}

class AstarSearch_Algorithm {

    int ROW, COL;
    AstartNode[][] Grid;

    AstartNode destNode;

    AstarSearch_Algorithm(int row, int col, int[][] grid) {
        ROW = row;
        COL = col;
        Grid = new AstartNode[ROW][COL];

        constructGrid(grid);
    }

    void constructGrid(int[][] grid){
        for(int r=0; r<ROW; r++){
            for(int c=0; c<COL; c++){

                Grid[r][c] = new AstartNode("[" + r + ", " + c + "]" );

                Grid[r][c].f = Integer.MAX_VALUE;
                Grid[r][c].g = Integer.MAX_VALUE;
                Grid[r][c].h = Integer.MAX_VALUE;
                Grid[r][c].parent = null;

                Grid[r][c].row = r;
                Grid[r][c].col = c;

                // wall or cost
                Grid[r][c].val = grid[r][c];
            }
        }
    }

    void setDestination(int[] dest){
        destNode = Grid[dest[0]][dest[1]];
    }

    // check given cell is a valid cell or not
    boolean isValid(int r, int c) {
        return (r >= 0) && (r < ROW) && (c >= 0) && (c < COL);
    }

    boolean isUnblocked(AstartNode node) {
        if(node.val == 0){
            return false;
        }else{
            return true;
        }
    }

    boolean isDestination(AstartNode node) {
        if(node == destNode){
            return true;
        }else{
            return false;
        }
    }

    ///////////////////////////////////////////////////////////////
    // Construct a shortest path
    ///////////////////////////////////////////////////////////////

    List<AstartNode> constructPath(AstartNode node) {
        LinkedList<AstartNode> path = new LinkedList<>();
        // dest node
//        path.add(node);

        while(node.parent != null){
            path.addFirst(node);
            node = node.parent;
        }

        // start node
        path.addFirst(node);
        return path;
    }

    ///////////////////////////////////////////////////////////////
    // Get Neighbors - For manhattan
    ///////////////////////////////////////////////////////////////

    ArrayList<AstartNode> getNeighbors_manhattan(AstartNode node){
        ArrayList<AstartNode> nodes = new ArrayList<>();

        // top
        if(node.row > 0){
//            Grid[node.row-1][node.col].parent = node;
            nodes.add(Grid[node.row-1][node.col]);
        }
        // bottom
        if(node.row+1 < ROW) {
//            Grid[node.row+1][node.col].parent = node;
            nodes.add(Grid[node.row+1][node.col]);
        }
        // left
        if(node.col > 0) {
//            Grid[node.row][node.col-1].parent = node;
            nodes.add(Grid[node.row][node.col-1]);
        }
        // right
        if(node.col+1 < COL) {
//            Grid[node.row][node.col+1].parent = node;
            nodes.add(Grid[node.row][node.col+1]);
        }

        return nodes;
    }

    ///////////////////////////////////////////////////////////////
    // Heuristics
    ///////////////////////////////////////////////////////////////

    // 1. Manhattan Distance = taxicab
    double heuristics_manhattan(int[] curr, int[] goal){
        return Math.abs(curr[0]-goal[0]) + Math.abs(curr[1]-goal[1]);
    }

    double heuristics_manhattan(AstartNode curr, AstartNode goal){
        return Math.abs(curr.row-goal.row) + Math.abs(curr.col-goal.col);
    }

    // allowed to move in eight directions
    double heuristics_diagonal(int[] curr, int[] goal){
        return Math.max(Math.abs(curr[0]-goal[0]), Math.abs(curr[1]-goal[1]));
    }

    // any direction
    double heuristics_euclidean(int[] curr, int[] goal){
        return Math.sqrt(Math.pow(curr[0]-goal[0], 2) + Math.pow(curr[1]-goal[1], 2));
    }

    ///////////////////////////////////////////////////////////////
    // A* Search
    ///////////////////////////////////////////////////////////////

    List<AstartNode> search(int[] start, int[] dest) {

        setDestination(dest);

        // SET OPENLIST - priority, CLOSEDLIST
        // <f, <i, j>>
        PriorityQueue<AstartNode> openlist = new PriorityQueue<>((a, b)-> ((int) (a.f - b.f)));
        boolean[][] closedList = new boolean[ROW][COL];

        // init startNode's parameters
        int i = start[0], j = start[1];
        Grid[i][j].f = 0.0;
        Grid[i][j].g = 0.0;
        Grid[i][j].h = 0.0;

        // 1. push startNode onto openlist

        openlist.offer( Grid[i][j] );

        while(!openlist.isEmpty()) {

            // find lowest f in openList
            AstartNode currNode = openlist.poll();

            // if currentNode is final, return the successful path
            if(isDestination(currNode)){
                currNode.parent = currNode;
                System.out.println("1 Destination is found through [" + currNode.row + ", " + currNode.col + "]");

                return constructPath(currNode);
            }

            // push currentNode onto closedList and remove from openList
            i = currNode.row;
            j = currNode.col;
            closedList[i][j] = true;

            // Generate each node_successor(neighbor)
            ArrayList<AstartNode> neighbors = getNeighbors_manhattan(currNode);

            // Foreach neighbor of currentNode
            for(AstartNode neighbor : neighbors) {

                if(isDestination(neighbor)){
                    double gNew = currNode.g + 1.0;
                    double hNew = heuristics_manhattan(neighbor, destNode);
                    double fNew = gNew + hNew;

                    neighbor.f = fNew;
                    neighbor.g = gNew;
                    neighbor.h = hNew;
                    neighbor.parent = currNode;

                    System.out.println("2 Destination is found through [" + currNode.row + ", " + currNode.col + "]");
                    System.out.println("f: " + neighbor.f);
                    System.out.println("g: " + neighbor.g);
                    System.out.println("h: " + neighbor.h);
                    return constructPath(neighbor);
                }

                boolean isInOpen = openlist.contains(neighbor);
                boolean isInClose = closedList[neighbor.row][neighbor.col];
                boolean isUnblocked = isUnblocked(neighbor);

                // if it is in closed or it is blocked then ignore it
                if(!isUnblocked){
                    continue;
                }
                else{
                    double gNew = currNode.g + neighbor.val;
                    double hNew = heuristics_manhattan(neighbor, destNode);
                    double fNew = gNew + hNew;

                    if(isInClose && neighbor.f < fNew) continue;

                    // OPEN : have been visited, but not expanded = the successors have not been explored yet
                    // CLOSE: have been visited and expanded.

                    else if(!isInOpen || neighbor.f > fNew ){

                        neighbor.f = fNew;
                        neighbor.g = gNew;
                        neighbor.h = hNew;
                        neighbor.parent = currNode;

                        if(isInClose){
                            closedList[neighbor.row][neighbor.col] = false;
                        }

                        if(!isInOpen){
                            openlist.add(neighbor);
                        }
                    }


                }
            } // end for(AstartNode neighbor : neighbors)

//            closedList[currNode.row][currNode.col] = true;
        }

        System.out.println("Failed to find the destination");
        return null;
    }
}

class AstartNode {

    AstartNode parent;

    // f = g + h
    // g(N) = exact cost from the starting point to any node N
    // h(N) = estimated cost from N to destination
    public double f, g, h;
    public String name;
    public int val;

    public int row, col;

    public AstartNode(int _val){
        val = _val;
    }

    public AstartNode(String nameVal){
        name = nameVal;
    }

    public AstartNode(String nameVal, int _val, double gVal, double hVal){
        name = nameVal;
        val = _val;
        g = gVal;
        h = hVal;
    }

    double getCost() {
        return g + h;
    }

    int compareTo(AstartNode other) {
        double thisValue = this.getCost();
        double otherValue = other.getCost();

        double diff = thisValue - otherValue;
        return (diff > 0) ? 1 : (diff == 0) ? 0 : -1;
    }

    @Override
    public String toString() {
        return "AstartNode" + name;
    }
}
