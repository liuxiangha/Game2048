package com.example.game2048;

public class Solver {
    private SearchNode currentNode;
    private SearchNode twincurrentNode;
    private Stack<Board> solution;

    private class SearchNode implements Comparable<SearchNode>{
        public Board board;
        public int moves;
        public SearchNode preSearchNode;

        public final int priority;

        public SearchNode(Board inboard, SearchNode inPreSearchNode){
            board = inboard;
            preSearchNode = inPreSearchNode;
            if(inPreSearchNode == null) moves = 0;
            else moves = inPreSearchNode.moves + 1;
            priority = moves + board.manhattan();
        }

        @Override
        public int compareTo(SearchNode o) {
            return Integer.compare(this.priority, o.priority);
        }
    }


    public Solver(Board initial) {
        // find a solution to the initial board (using the A* algorithm)
        if(initial == null)
            throw new IllegalArgumentException("Constructor argument Board is null!");
        currentNode = new SearchNode(initial,null);
        twincurrentNode = new SearchNode(initial.twin(),null);
        MinPQ<SearchNode> priorityQueue = new MinPQ<SearchNode>();
        MinPQ<SearchNode> twinPriorityQueue = new MinPQ<SearchNode>();
        priorityQueue.insert(currentNode);
        twinPriorityQueue.insert(twincurrentNode);
        while(true){
            currentNode = priorityQueue.delMin();
            if(currentNode.board.isGoal()) break;
            putNeighBorsIntoPQ(currentNode,priorityQueue);

            twincurrentNode = twinPriorityQueue.delMin();
            if(twincurrentNode.board.isGoal()) break;
            putNeighBorsIntoPQ(twincurrentNode,twinPriorityQueue);
        }
    }

    private void putNeighBorsIntoPQ(SearchNode searchNode, MinPQ<SearchNode> pq){
        Iterable<Board> neighbors = searchNode.board.neighbors();
        for(Board neighbor : neighbors) {

            if(searchNode.preSearchNode==null || !neighbor.equals(searchNode.preSearchNode.board))
                pq.insert(new SearchNode(neighbor,searchNode));
        }
    }

    public boolean isSolvable() {
        // is the initial board solvable?
        return currentNode.board.isGoal();
    }

    public int moves() {
        // min number of moves to solve initial board; -1 if unsolvable
        if(currentNode.board.isGoal())
            return currentNode.moves;
        else
            return -1;
    }

    public Iterable<Board> solution() {
        // sequence of boards in a shortest solution; null if unsolvable
        if(currentNode.board.isGoal()){
            solution = new Stack<Board>();
            SearchNode node = currentNode;
            while(node != null){
                solution.push(node.board);
                node = node.preSearchNode;
            }
            return solution;
        }else
            return null;
    }
}
