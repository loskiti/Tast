package Paint;

public abstract class BaseActionUnitSprite extends UnitSprite {
	 /**
     * Действие шагания персонажа.
     */
    public void actionStep() {
        if(imageMaxX == 10 && imageMinX == 4) {
            return;
        }
        imageMaxX = 10;
        imageMinX = imageX = 4;
    }
    
    /**
     * Действие стояния на месте персонажа.
     */
    public void actionStand() {
        if(imageMaxX == 3 && imageMinX == 0) {
            return;
        }
        imageMinX = imageX = 0;
        imageMaxX = 3;        
    }
    
    /**
     * Поворот наверх.
     */
    public void turnTop() {
    	imageMinY = imageMaxY = imageY = 3;
    }
    
    /**
     * Поворот налево.
     */
    public void turnLeft() {
    	imageMinY = imageMaxY = imageY = 1; 
    }
    
    /**
     * Поворот направо.
     */
    public void turnRight() {
    	imageMinY = imageMaxY = imageY = 5; 
    }
    
    /**
     * Поворот вниз.
     */
    public void turnBoth() {
    	imageMinY = imageMaxY = imageY = 7;
    }
    
    @Override
    public void update() {
        super.update();
        //указываем действие и поворот по умолчанию
        if(directX == 0 && directY == 0) {
            actionStand();
        } else if(directX == 0 && directY == 1) {
            turnBoth();
            actionStep();
        } else if(directX == 0 && directY == -1) {
            turnTop();
            actionStep();
        } else if(directX == 1 && directY == 0) {
            turnRight();
            actionStep();
        } else if(directX == -1 && directY == 0) {
            turnLeft();
            actionStep();
        }        
    }
    public void update1(int dirX,int dirY){
    	imageX++;
		if (imageX > imageMaxX) {
			imageX = imageMinX;
		}
		imageY++;
		if (imageY > imageMaxY) {
			imageY = imageMinY;
		}
    	 if(dirX == 0 && dirY == 0) {
             actionStand();
         } else if(dirX == 0 && dirY == 1) {
             turnBoth();
             actionStep();
         } else if(dirX == 0 && dirY == -1) {
             turnTop();
             actionStep();
         } else if(dirX == 1 && dirY == 0) {
             turnRight();
             actionStep();
         } else if(dirX == -1 && dirY == 0) {
             turnLeft();
             actionStep();
         }        
    }
}
