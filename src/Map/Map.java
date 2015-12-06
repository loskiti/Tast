package Map;

//карта
public abstract class Map {
protected int map[][];

public int getWidth() {
	return map[0].length;
}
public int getHeight() {
	return map.length;
}
// есть ли €чейка
public boolean Tile(int x, int y){
	if ((x >= 0) && (y >= 0) && (y <  getHeight()) && (x <  getWidth()))
	return true;
	return false;
}

public int getTile(int x, int y){
	if (Tile(x,y))
		return map[y][x];
	return 0;
	
}

}

