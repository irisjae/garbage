package garbage;

import java.io.Serializable;

public class UserStat implements Serializable {
	public int winsCount;
	public int gamesCount;
	public float winsTimesAverage;
	public int rank;

	public UserStat () {
		this (0, 0, 0); }
	public UserStat (int winsCount, int gamesCount, float winsTimesAverage) {
		this (winsCount, gamesCount, winsTimesAverage, 0); }
	public UserStat (int winsCount, int gamesCount, float winsTimesAverage, int rank) {
		this .winsCount = winsCount;
		this .gamesCount = gamesCount;
		this .winsTimesAverage = winsTimesAverage; 
		this .rank = rank; } }