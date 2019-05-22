package garbage;

import java.io.Serializable;

public class UserInfo implements Serializable {
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