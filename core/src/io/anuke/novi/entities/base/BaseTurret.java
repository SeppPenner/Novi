package io.anuke.novi.entities.base;

import com.badlogic.gdx.math.MathUtils;

import io.anuke.novi.Novi;
import io.anuke.novi.entities.basic.Bullet;
import io.anuke.novi.items.ProjectileType;
import io.anuke.novi.network.SyncData;
import io.anuke.novi.utils.Draw;

public class BaseTurret extends BaseBlock{
	float lastrotation = 0;
	{
		health = 100;
	}
	
	@Override
	public void draw(){
		rotation = MathUtils.lerpAngleDeg(rotation, lastrotation, 0.1f);
		
		Draw.rect("ironblock", x, y, base.rotation);
		Draw.rect("turret", x, y, rotation);
	}

	@Override
	public void behaviorUpdate(){
		
		if(target == null)
			return;

		rotation = autoPredictTargetAngle(x, y, 4f) + 90;
		reload += Novi.delta();

		if(reload >= 100){
			new Bullet(ProjectileType.redbullet, rotation + 90).setShooter(base).set(x, y).translate(3, 5).add().send();
			new Bullet(ProjectileType.redbullet, rotation + 90).setShooter(base).set(x, y).translate(-3, 5).add().send();

			reload = 0;
		}
	}
	
	@Override
	public boolean sync(){
		return true;
	}
	
	@Override
	public SyncData writeSync(){
		return new SyncData(getID(), rotation);
	}
	
	@Override
	public void readSync(SyncData data){
		this.lastrotation = data.get(0);
	}
}
