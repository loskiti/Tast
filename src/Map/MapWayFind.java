package Map;
/**
 * Реализация пузырькового алгоритма поиска пути
 */

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import Map.WayPoint;
public class MapWayFind {
	private MapWay mapway;
	/**
	 * можно ли пройти по плитке
	 */
	private IMapCheckPoint checkPoint;
	/**
	 * дошли ли до конца
	 */
	private boolean isFinish;
	/**
	 * потенциально возможные точки пути
	 */
	private List<WayPoint> nextPoint;
	/**
	 * уже найденные точки пути
	 */
	private List<WayPoint> backPoint;

	public MapWay getWay() {
		return mapway;
	}

	/**
	 * проверка прохождения
	 */
	public void setcheckPoint(IMapCheckPoint checkPoint) {
		this.checkPoint = checkPoint;
	}

	/**
	 * поиск пути
	 */
	public boolean findWay(int startx, int starty, int endx, int endy) {
		isFinish = false;
		mapway = new MapWay();
		nextPoint = new ArrayList<WayPoint>();
		backPoint = new ArrayList<WayPoint>();

		WayPoint p = new WayPoint(startx, starty, -1, -1, Math.abs(startx - endx) + Math.abs(starty - endy), true);
		nextPoint.add(p);
		backPoint.add(p);
		WayPoint node;
		while (nextPoint.size() > 0) {
			// Берем первую потенциальную точку из списка.

			node = nextPoint.get(0);
			nextPoint.remove(0);

			// проверяем дошли ли мы до конца

			if (node.getX() == endx && node.getY() == endy) {

				// создаем путь
				makeWay(node);
				isFinish = true;
				break;
			} else {
				// Помечаем точку как просмотренную, что бы не уйти в вечный
				// цикл
				node.setVisited(true);
				// ставим все потенциально возможные точки
				addNode(node, node.getX() + 1, node.getY(), endx, endy);
				addNode(node, node.getX() - 1, node.getY(), endx, endy);
				addNode(node, node.getX(), node.getY() + 1, endx, endy);
				addNode(node, node.getX(), node.getY() - 1, endx, endy);

			}
		}

		backPoint = null;
		nextPoint = null;

		return isFinish;
	}

	/**
	 * Создание пути из отобранных точек
	 */

	private void makeWay(WayPoint node) {
		mapway.clear();
		while (node.getPX() != -1) {
			mapway.addPoint(new Point(node.getX(), node.getY()));
			// ищем предыдущию
			for (WayPoint p : backPoint) {
				if (p.getX() == node.getPX() && p.getY() == node.getPY()) {
					node = p;
					break;
				}
			}
		}
	}

	/**
	 * добавляем возможную точку
	 */

	private void addNode(WayPoint node, int x, int y, int endx, int endy) {
		if (checkPoint.check(x, y)) {
			int cost = Math.abs(x - endx) + Math.abs(y - endy);
			WayPoint px = new WayPoint(x, y, node.getX(), node.getY(), cost, false);
			WayPoint old = null;

			// проверяем точку на уникальность (p-начало)
			for (WayPoint p : backPoint) {
				if (p.getX() == px.getX() && p.getY() == px.getY()) {
					old = p;
					break;
				}
			}
			// Точка уникальна, или стоимость новой точки меньше старой
			if (old == null || old.getCost() > cost) {
				backPoint.add(px);
				int i = 0;
				for (i = 0; i < nextPoint.size(); i++) {
					// Ставим точку с меньшой стоимостью в приоритет обхода
					// потенциальных точек.
					if (cost < nextPoint.get(i).getCost()) {
						nextPoint.add(i, px);
						break;
					}
				}
				// если точка не была вставлена, ставим ее вконец
				if (i >= nextPoint.size()) {
					nextPoint.add(px);
				}
			}
		}
	}
}
