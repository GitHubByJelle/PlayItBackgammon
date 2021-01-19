# PlayItBackgammon
* To play the game with the GUI run the Main class. 
* To use the bots for testing, run the BotTestingGround class(in AI package) with executeTourney(int numGames) in the Main method and a given number of games as a parameter.
  * The Bots included in the tournament are in Variables.BOTS.
  * To execute a one-bot-tournament(only combinations with the given bot), have only executeOneBotTourney(int numGamesPerCombination, int secondplayer) in the Main method where int secondPlayer is the index of the wanted bot in Variables.BOTS.
* To train TD-Gammon Bot, run the NN(in AI.TDGammon package) class with trainDataTD(int NumberOfGames, boolean Save, int offSet) in the main method with a given number of games, a boolean for saving every 50,000 games, and offset as to which save file you start with.
  * (For example; with an offset of 900, you will start at "900k" and the first save file produced will be "950k").
  * To use a new file, it needs to be imported in TDG class as neuralNet using the NNFile.Import(String filename) method. 
* To train Trained Measures Model bot run the Evolution class(in AI.GA package), start a new population, and call Population.populationEvolver().
* WARNING: AlphaBetaBot cannot play as Player 2 in any circumstance. 
* WARNING: The Alpha-Beta bot gives rare errors and infinite loops when running as player 1. Therefore, when running the BotTestingGround for Alpha-Beta, the numGames parameter in  executeTourney(int numGames) should be set to a small number, i.e 10 or 100
