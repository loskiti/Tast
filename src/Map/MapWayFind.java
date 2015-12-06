package Map;
//пузырьковый алгоритм
import java.awt.Point;
import java.util.ArrayList;


public class MapWayFind {
protected MapWay mapway;
//можно ли пройти по плитке
protected IMapCheckPoint checkPoint;
// дошли ли до конца
protected boolean isFinish;
//потенциально возможные точки пути
protected ArrayList<WayPoint> nextPoint;
// уже найденные
protected ArrayList<WayPoint> backPoint;

public MapWay getWay(){
	return mapway;
}
//проверка прохождения
public void setcheckPoint(IMapCheckPoint checkPoint) {
    this.checkPoint = checkPoint;
}
public boolean findWay( int startx, int starty, int endx, int endy) {
	isFinish = false;
    mapway = new MapWay();
    nextPoint = new ArrayList<WayPoint>();
backPoint = new ArrayList<WayPoint>();
    
    WayPoint p = new WayPoint(startx, starty, -1, -1, Math.abs(startx-endx) + Math.abs(starty-endy), true);
    nextPoint.add(p);
    backPoint.add(p);
    WayPoint node;
    while(nextPoint.size() > 0) {
        //Берем первую потенциальную точку из списка.
        node = nextPoint.get(0);
        nextPoint.remove(0);
        
        //проверяяем дошли ли мы до конца.
        if (node.x == endx && node.y == endy) {
            //создаем путь
            makeWay(node);
            isFinish = true;
            break;
        } else {
            //Помечаем точку как просмотренную, что бы не уйти в вечный цикл
            node.visited = true;
            //ставим все потенциально возможные точки
            addNode (node, node.x+1, node.y, endx, endy);
            addNode (node, node.x-1, node.y, endx, endy);
            addNode (node, node.x, node.y+1, endx, endy);
            addNode (node, node.x, node.y-1, endx, endy); 
            
        }       
    }
    
    backPoint = null;
    nextPoint = null;
    
    return isFinish;
}
//путь из отобранных точек
protected void makeWay(WayPoint node) {
    mapway.clear();    
    while (node.px != -1){  
        mapway.addPoint(new Point(node.x, node.y)); 
        //ищем предыдущию
        for(WayPoint p : backPoint) {
            if(p.x == node.px && p.y == node.py) {
                 node = p;
                 break;
            }
        } 
    }  
}
// добавляем возможную точку
protected void addNode(WayPoint node,  int x, int y, int endx, int endy) {
    if(checkPoint.check(x ,y)) {
		int cost = Math.abs(x-endx)+Math.abs(y-endy);
        WayPoint px = new WayPoint(x, y, node.x, node.y, cost, false);
        WayPoint old = null;
    
        //проверяем точку на уникальность (p-начало)
        for(WayPoint p : backPoint) { 
            if(p.x == px.x && p.y == px.y) {
                old = p;
                break;
            }
        }
        //Точка уникальна, или стоимость новой точки меньше старой
    if(old == null || old.cost > cost) {
        backPoint.add(px);
        int i = 0;
        for(i=0; i < nextPoint.size(); i++){
            //Ставим точку с меньшой стоимостью в приоритет обхода потенциальных точек.
            if (cost < nextPoint.get(i).cost){ 
                 nextPoint.add(i, px);
                 break;
            }
        }
        //если точка не была вставлена, ставим ее вконец
       if (i >= nextPoint.size()) {
            nextPoint.add(px);
        }
    }  
}
} 
}

/*class WayPoint {
    public int x, y, px, py, cost;
    public boolean visited; 
    
    
    public  WayPoint(int x, int y, int px, int py, int cost, boolean visited) {
		
       setData(x, y, px, py,cost, visited); 
    }
    
    final public void setData(int x, int y, int px, int py, int cost, boolean visited) {
        this.x = x;
        this.y = y;
        this.px = px;
        this.py = py;
        this.cost = cost;
        this.visited = visited;
         
    } 
}  
}*/