import java.util.*;

/****************************************************************
 * schaumberg2Player.java
 * Implements MiniMax search with A-B pruning and iterative deepening search 
 * (IDS). 
 * The static board evaluator (SBE) function is simple: 
 * the # of stones in studPlayer's mancala minus the # in opponent's mancala.
 * ---------------------------------------------------------------------------
 * Licensing Information: You are free to use or extend these projects for 
 * educational purposes provided that
 * (1) you do not distribute or publish solutions, 
 * (2) you retain the notice, and 
 * (3) you provide clear attribution to UW-Madison
 *
 * Attribute Information: The Mancala Game was developed at UW-Madison.
 *
 * The initial project was developed by Chuck Dyer(dyer@cs.wisc.edu) and his TAs.
 *
 * Current Version with GUI was developed by Fengan Li(fengan@cs.wisc.edu).
 * Some GUI componets are from Mancala Project in Google code.
 */




//################################################################
// studPlayer class
//################################################################

public class schaumberg2Player extends Player 
{
	/*Use IDS search to find the best move. The step starts from 1 and keeps 
	 * incrementing by step 1 until
	 * interrupted by the time limit. The best move found in each step should be stored in the
	 * protected variable move of class Player.
	 * 
	 * HW2 Notes:
	 * This method will implement the IDS algorithm to update the protected data
	 * member move after each iteration of IDS. (The getMove() method then 
	 * returns that data member to the class that is controlling the game 
	 * environment.) The maxDepth of the IDS algorithm starts from 1 and 
	 * increments by 1 at each iteration until interrupted due to the time 
	 * limit. Inside each iteration, you need to do a Minimax search with 
	 * maxDepth. Note: When move(GameState state) is called, the bottom row of
	 * the board is your (i.e., the AI’s) side. Be sure that when you start 
	 * each new iteration of IDS you get a copy of the current state before 
	 * changing it.
	 * 
	 */
	public void move(GameState state)
	{
		// when move is called, IDS begins with a depth limitation of 1
		int depthVar = 1;
		// Technically an infinite loop, however the return value of the
		// maxAction wrapper will set move to the AI's selected state.
		while(true)
		{
			this.move = maxAction(state, depthVar);
			// increase depth limitation at each iteration, until 
			// the AI is capable of finding the necessary move.
			depthVar++;
		}
	}

	/*
	 *  Return best move for max player. Note that this is a wrapper function 
	 *  created for ease to use. In this function, you may do one step of 
	 *  search. Thus you can decide the best move by comparing the sbe
	 *  values returned by maxSBE. This function should call minAction with
	 *  5 parameters.
	 *  
	 *  HW2 Notes:
	 *  This is a wrapper function for Minimax search for ease of use. 
	 *  The caller of this function should be the Max player. The detailed 
	 *  descriptions of input and output are given below: 
	 *  o @GameState state: The game state for the current player 
	 *  o @int maxDepth: The maximum depth you can search in the search tree 
	 *  o @return: Return the best move that leads to the maximum SBE value.
	 *  You may alter this to return the (best move, SBE value) if you want.
	 *  
	 */
	public int maxAction(GameState state, int maxDepth)
	{
		// set to 0, will be modified in move function.
		int currentDepth = 0;
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		int optimumPly = 
				maxAction(state, currentDepth, maxDepth, alpha, beta).getBinID();
		return optimumPly;
	}
	/*
	 * HW2 Notes:This function is similar to 
	 * public int maxAction(GameState state, int maxDepth) 
	 * except this function returns the best move for the Min player.
	 * 
	 *  IMPORTANT NOTE: 
	 *  this function will never be used within this program, 
	 *  since the AI always begins from the maxAction wrapper function.
	 */
	public int mminAction(GameState state, int maxDepth)
	{
		return 0;
	}

	/*
	 * return SBE value related to the best move for max player
	 * 
	 * HW2 Notes:This function will actually do the Minimax search with 
	 * Alpha-Beta pruning. The detailed descriptions of input and output are 
	 * given below:
	 * o @GameState state: The game state we are currently searching
	 * o @int currentDepth: The current depth of the game state we are searching
	 * o @int maxDepth: The maximum depth we can search. When current depth 
	 *   equals maxDepth, we should stop searching and call the SBE function 
	 *   to evaluate the game state 
	 * o @int alpha: This variable is for alpha-beta pruning, which should be 
	 *               self explanatory. 
	 * o @int beta: This variable is similar to alpha
	 * o @return: Return the best move that leads to the child that gives the 
	 *   maximum SBE value; return the move with the smallest index in the case 
	 *   of ties. You may alter this to return the (best move, SBE value)
	 *   if you want.
	 *   
	 * It is important to note that we will also call the SBE function to 
	 * evaluate the game state when the game is over, i.e., when someone has 
	 * won the game. 
	 *   
	 */
	public SBEvalPair maxAction(GameState state, int currentDepth, int maxDepth, int alpha, int beta)
	{
		// Check state, verify if a Terminal Board State.
		if(state.gameOver())
		{
			// The value of the bin is unknown. Value will be determined 
			// within the maxAction method above.
			SBEvalPair sbePair = new SBEvalPair(0, sbe(state));
			return sbePair;
		}
		// Value of Static Board Eval Pair used to determine initial cut off 
		SBEvalPair sbePair = new SBEvalPair(0,Integer.MIN_VALUE);
		// If there are branches of our GameState left to expand/explore
		// Continue on.
		if(currentDepth < maxDepth)
		{
			// Loop through each potential move for the given Board State
			for(int i = 0; i < 6; i++)
			{
				// Verify the potential move being made adheres to the rules of 
				// the board game; if so, continue.
				if(!state.illegalMove(i))
				{
					// Make a copy of the current state
					GameState childState = new GameState(state);
					// This copy will be used to create theoretical or 
					// potential moves without overwriting our current state
					boolean takeExtraTurn = childState.applyMove(i);
					SBEvalPair tempSBEPair;
					if(takeExtraTurn)
					{
						// If the AI is able to make a move enabling an extra
						// turn, recurse the tempSBEPair and use that state
						// as the next turn to decide what moves to make from
						// that Game State.
						tempSBEPair = maxAction(childState, currentDepth +1,
								maxDepth, alpha, beta);
					}
					else
					{
						// Recurse the temp pair, prepare the AI to 
						// expand/explore the  optimal minimum for the opposing 
						// player's next move. 
						tempSBEPair = minAction(childState, currentDepth +1,
								maxDepth, alpha, beta);
					}
					
					if(sbePair.compareTo(tempSBEPair) < 0)
					{
						tempSBEPair.setBinID(i);
						sbePair = tempSBEPair;
					}
					if(sbePair.getSBEValue() >= beta)
					{
						return sbePair;
					}
					// set alpha threshold
					alpha = Math.max(alpha, sbePair.getSBEValue());
				}
			}
		}
		// If the current depth is equivalent to the maximum depth,
		// Then we must set our sbePair's value to the current state 
		else
		{
			sbePair.setSBEValue(sbe(state));
		}
		return sbePair;
	}

	/*return sbe value related to the bset move for min player
	 * 
	 * This function is similar to public int maxAction(GameState state, int 
	 * currentDepth, int maxDepth, int alpha, int beta) except this function 
	 * returns the best move for the Min player. 
	 */
	public SBEvalPair minAction(GameState state, int currentDepth, int maxDepth,
			int alpha, int beta)
	{
		// Check state, verify if a Terminal Board State.
		if(state.gameOver())
		{
			SBEvalPair sbePair = new SBEvalPair(0, sbe(state));
			return sbePair;
		}
		// Value of Static Board Eval Pair used to determine pruning
		SBEvalPair sbePair = new SBEvalPair( 0, Integer.MAX_VALUE );
		// If there are branches of our GameState left to expand/explore
		// Continue on.
		if( currentDepth < maxDepth )
		{
			// Loop through each potential move for the given Board State
			for(int i = 0; i < 6; i++)
			{
				// Verify the potential move being made adheres to the rules of 
				// the board game; if so, continue.
				if(!state.illegalMove(i))
				{
					// Make a copy of the current state
					GameState childState = new GameState(state);
					// This copy will be used to create theoretical or 
					// potential moves without overwriting our current state
					boolean takeExtraTurn = childState.applyMove(i);
					SBEvalPair tempSBEPair;
					if(takeExtraTurn)
					{
						// If the opponent is able to make a move enabling an 
						// extra turn, recurse the tempSBEPair and use state
						// as the next turn to decide what moves to make from
						// that Game State.
						tempSBEPair = minAction(childState, currentDepth + 1,
								maxDepth, alpha, beta);
					}
					else
					{
						// Recurse the temp pair state, prepare the AI to 
						// expand/explore the optimal maximum for it's 
						// next move. 
						tempSBEPair = maxAction(childState, currentDepth + 1,
								maxDepth, alpha, beta);
					}
					if(sbePair.compareTo(tempSBEPair) < 0)
					{
						tempSBEPair.setBinID(i);
						sbePair = tempSBEPair;
					}
					if(sbePair.getSBEValue() <= alpha)
					{
						return sbePair;
					}
					// set beta threshold
					beta = Math.min(beta, sbePair.getSBEValue());
				}
			}
		}
		// If the current depth is equivalent to the maximum depth,
		// Then we must set our sbePair's value to the current state 
		else
		{
			sbePair.setSBEValue(sbe(state));
		}
		return sbePair;
	}

	/*
	 * the sbe function for game state. Note that in the game state, 
	 * the bins for current player are always in the bottom row. 
	 * 
	 * This will calc the Static Board Evaluation function:
	 *(AI's game pieces in the mancala + AI's game pieces on the board) 
	 * - (opponent game pieces in their mancala + opponent game pieces on the board)  
	 *      = SBE Value
	 * 
	 */
	private int sbe(GameState state)
	{
		int sbeVal = 0;
		for(int i = 0; i < 7; i++)
		{
			sbeVal += state.stoneCount(i);
		}
		for(int i = 7; i < 14; i++)
		{
			sbeVal -= state.stoneCount(i);
		}
		return sbeVal;
	}
}

