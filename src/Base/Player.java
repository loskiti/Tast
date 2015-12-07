package Base;
/**
 *Игрок
 */

import Paint.BaseActionUnitSprite;

public class Player extends BaseActionUnitSprite {

	public Player() {
		String name = "/data/hero/warr.png";
		offsetRenderY = -74;
		offsetRenderX = -27;
		paintWidth = 128;
		paintHeight = 128;
		layerDeep = 10000;
		initiation(name);
		actionStand();
		turnRight();
	}

}
