package Paint;

public abstract class BaseActionUnitSprite extends UnitSprite {
	 /**
     * Сделать шаг
     */
    private void actionStep() {
        if(imageMaxX == 10 && imageMinX == 4) {
            return;
        }
        imageMaxX = 10;
        imageMinX = imageX = 4;
    }
    
    /**
     * Стоять на месте 
     */
    public void actionStand() {
        if(imageMaxX == 3 && imageMinX == 0) {
            return;
        }
        imageMinX = imageX = 0;
        imageMaxX = 3;        
    }
    
    /**
     * Поворот наверх
     */
    public void turnTop() {
    	imageMinY = imageMaxY = imageY = 3;
    }
    
    /**
     * Поворот налево
     */
    public void turnLeft() {
    	imageMinY = imageMaxY = imageY = 1; 
    }
    
    /**
     * Поворот направо
     */
    public void turnRight() {
    	imageMinY = imageMaxY = imageY = 5; 
    }
    
    /**
     * Поворот вниз
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
    
}
