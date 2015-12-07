package Map;

/**
 * Информация о гененрирующемся пути
 */
import java.awt.Point;
import java.util.ArrayList;
import Paint.Tile;

public class MapWay {
	public ArrayList<Point> way;
	public Point p;
	/**
	 * Текущая позиция
	 */
	public int index = 0;

	public MapWay() {
		way = new ArrayList<Point>();
	}

	public void clear() {
		way.clear();
	}

	public void addPoint(Point p) {
		way.add(p);
	}

	/**
	 * начальная точки
	 */
	public void startPoint(int x, int y) {
		p = new Point(x, y);
		index = way.size() - 1;
	}

	/**
	 * существует ли точка
	 */

	public boolean isNextPoint() {
		if (index > -1)
			return true;
		return false;
	}

	/**
	 * Перейти на следующую точку (если текущую уже прошли)
	 */

	public Point nextPoint(int step) {
		int x = way.get(index).x * Tile.SIZE, y = way.get(index).y * Tile.SIZE;

		if (p.x != x) {
			p.x += step * ((x < p.x) ? -1 : 1);
		}
		if (p.y != y) {
			p.y += step * ((y < p.y) ? -1 : 1);
		}

		if (p.x == x && p.y == y) {
			index--;
		}

		return p;
	}
}
