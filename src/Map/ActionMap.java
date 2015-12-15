package Map;

/**
 * Действия с картой
 */
public abstract class ActionMap {
	protected int map[][];
	//protected String map[][];
	public int getWidth() {
		return map[0].length;
	}

	public int getHeight() {
		return map.length;
	}

	/**
	 * Возвращает boolean есть ли ячейка
	 */
	private boolean isTile(int x, int y) {
		if ((x >= 0) && (y >= 0) && (y < getHeight()) && (x < getWidth()))
			return true;
		return false;
	}

	/**
	 * Возвращает значение ячейки
	 */
	public int getTile(int x, int y) {
		if (isTile(x, y))
			return map[y][x];
		return 0;

	}
	/*public String getTile(int x, int y) {
		if (isTile(x, y))
			return map[y][x];
		return null;

	}*/
}
