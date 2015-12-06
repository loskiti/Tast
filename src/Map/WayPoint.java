package Map;
//точка пути
public class WayPoint {
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
