package garbage;

public class UserInfo {
	public int winsCount;
	public int gamesCount;
	public float winsTimesAverage;
	public int rank;

	public UserInfo () {
		this (0, 0, 0); }
	public UserInfo (int winsCount, int gamesCount, float winsTimesAverage) {
		this (winsCount, gamesCount, winsTimesAverage, 0); }
	public UserInfo (int winsCount, int gamesCount, float winsTimesAverage, int rank) {
		this .winsCount = winsCount;
		this .gamesCount = gamesCount;
		this .winsTimesAverage = winsTimesAverage; 
		this .rank = rank; } }