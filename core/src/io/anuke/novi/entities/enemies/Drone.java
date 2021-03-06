package io.anuke.novi.entities.enemies;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import io.anuke.novi.effects.BreakEffect;
import io.anuke.novi.entities.base.Base;
import io.anuke.novi.graphics.Wrap;
import io.anuke.novi.items.ProjectileType;
import io.anuke.novi.modules.World;

public class Drone extends Enemy{
	transient float speed = 0.1f;
	transient float turnrange = 80;
	transient float offset;
	transient float sign = 0;
	public transient Base base;
	
	{
		material.maxvelocity = 2f;
		material.getRectangle().setSize(10);
		health = 10;
		offset = MathUtils.random(-30,30);
		sign = MathUtils.randomSign();
		speed += MathUtils.random(0.3f);
	}
	
	@Override
	public void draw(){
		Wrap.rect("drone", x, y, velocity.angle() - 90);
	}
	
	public void onDeath(){
		super.onDeath();
		if(base != null) base.spawned --;
		new BreakEffect("drone", velocity, 0.2f).set(x, y).add();
	}

	@Override
	public void behaviorUpdate(){
		if(target == null) return;
		Vector2 add = new Vector2(World.relative3(target.x, x), World.relative3(target.y, y));
		
		float len = add.len();
		float anglechange =  sign*(turnrange - len)*(90f/turnrange);
		if(len < turnrange) add.setAngle((add.angle() + anglechange));
		add.setAngle(add.angle() + offset);
		velocity.add(add.setLength(speed * delta()));
		
		if(MathUtils.randomBoolean(0.03f)){
			shoot(ProjectileType.redbullet, targetAngle()-180);
		}
	}

}
