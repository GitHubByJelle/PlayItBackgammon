# PlayItBackgammon
This project is part of a group project for the for the bachelor programm Data Science. The project contains an implementation of [Backgammon](https://www.chessprogramming.org/Backgammon), as well as implementation of search algorithms that can handle the imperfect and complete information of the game.

<p align="center" width="100%">
    <img src="pics/backgammon.gif" alt="Visualisation of TDGammon and TMM playing a game against each other." width="70%">
</p>

## Implementation details
The code is written in Java. No packages have been used for the implementations of the visualisations or search algorithms. For AI functionality, the code leverages the following techniques/algorithms:
* One-ply search
* Alpha-Beta Search
* TDGammon
* Neural Networks
* Genetic Algorithm

Based on these algorithms, combined with different feature evaluation functions, multiple bots have been implemented:
* RandomBot
* SimpleBot (plays first legal move)
* PrimeBlitzingBot (one-ply search with simple evaluation function)
* Trained Measures Model (TMM) (two-ply search with more sophisticated evaluation function trained with Genetic Algorithm)
* AlphaBetaBot (Alpha beta search)
* [TD-Gammon](https://www.csd.uwo.ca/~xling/cs346a/extra/tdgammon.pdf)

## How to use
To play the game with the GUI, run the Main.java class.

To use the bots for testing, run the BotTestingGround class(in AI package) with executeTourney(int numGames) in the Main method and a given number of games as a parameter.

To execute a one-bot-tournament (only combinations with the given bot), have only executeOneBotTourney(int numGamesPerCombination, int secondplayer) in the Main method where int secondPlayer is the index of the wanted bot in Variables.BOTS.

To train TD-Gammon Bot, run the NN(in AI.TDGammon package) class with trainDataTD(int NumberOfGames, boolean Save, int offSet) in the main method with a given number of games, a boolean for saving every 50,000 games, and offset as to which save file you start with. (For example; with an offset of 900, you will start at "900k" and the first save file produced will be "950k"). To use a new file, it needs to be imported in TDG class as neuralNet using the NNFile.Import(String filename) method. 

To train Trained Measures Model bot run the Evolution class(in AI.GA package), start a new population, and call Population.populationEvolver().

## Known bugs for improvements
* AlphaBetaBot cannot play as Player 2 in any circumstance. 
* The Alpha-Beta bot gives rare errors and infinite loops when running as player 1. Therefore, when running the BotTestingGround for Alpha-Beta, the numGames parameter in  executeTourney(int numGames) should be set to a small number, i.e 10 or 100
