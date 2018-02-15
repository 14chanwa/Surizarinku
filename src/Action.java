public class Action {
	int x;
	int y;
	Direction d;

	enum Direction {
		H, B, G, D;
	}

	public Action(int coordX, int coordY, Direction directD) {
		x = coordX;
		y = coordY;
		d = directD;
	}

}
