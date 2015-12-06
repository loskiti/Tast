package Paint;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;

import Base.Game;
import Map.MapWay;

public abstract class Unit implements IRenderToConvas {
	// картинка 
		protected Image imageUnit;
		// реальные координаты персонажа
		public int X;
		public int Y; 
		public int layerDeep;
	    //координаты, видимые на экране
	        public int posUnitY;
	        public int posUnitX;
		public int speed=5;
		 public int width = 32;
		 public int height = 32; 
		public int directX=0;
		public int directY=0;
		 public MapWay mapway;
		 //Отступ при рисовании объекта на карте, что бы он занял середину клетки
		 public int offsetRenderX = 0;
		 public int offsetRenderY = 0;
		 public void initiation(String name) { 
		        imageUnit = new ImageIcon(getClass().getResource(name)).getImage();
		    }
		 @Override
		    public void render(Graphics g) {  
		        
		        update();
		       /* int isoX = posUnitX,
		                isoY = posUnitY;
		            if(Game.USE_ISO) {
		            	isoX = (posUnitX - posUnitY); 
		            	isoY = (posUnitX + posUnitY) / 2;  
		            }
		        g.drawImage(imageUnit,isoX + offsetRenderX + Game.OFFSET_MAP_X, isoY + offsetRenderY, width, height, null); */
		       // g.drawImage(imageUnit, posUnitX, posUnitY, width, height, null); 
		        int isoX = posUnitX,
		                isoY = posUnitY;
		            if(Game.USE_ISO) {
		                 isoX = (posUnitX - posUnitY); 
		                 isoY = (posUnitX + posUnitY) / 2;  
		            }
		            g.drawImage(imageUnit, isoX + offsetRenderX + Game.OFFSET_MAP_X, isoY + offsetRenderY, width, height, null); 
		 }
		/* @Override
		    public int getDeep() {
		        return Y * Canvas.LAYER_DEEP1 + X;
		    }*/
		 //определение направления следущий точки (для мышки, на клаве авто, тут в зависимости от следущий точки пути до цели)
		 public void update(){
				if(mapway != null && mapway.isNextPoint()) {
			        if(mapway.p == null) {
			           mapway.startPoint(X, Y);
			        } else {
			            mapway.nextPoint(speed);
			            if(X != mapway.p.x) {
			                directX = (X > mapway.p.x) ? -1 : 1;
			            } else {
			                 directX = 0;
			            }
			            if(Y != mapway.p.y) {
			                directY = (Y > mapway.p.y) ? -1 : 1;
			            } else {
			                 directY = 0; 
			            }
			        } 
			    } else {
			       directX = 0;
			       directY = 0; 
			    }
			}
		 public int getDeep() {
			
		     //   return Y * layerDeep + X;
			 return layerDeep;
			// return layerDeep;
		    } 
}
