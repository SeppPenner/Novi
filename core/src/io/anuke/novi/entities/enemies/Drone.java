package io.anuke.novi.entities.enemies;

import com.badlogic.gdx.math.*;

import io.anuke.novi.entities.*;
import io.anuke.novi.entities.effects.BreakEffect;
import io.anuke.novi.items.ProjectileType;
import io.anuke.novi.utils.WorldUtils;

public class Drone extends Enemy{
	float speed = 0.2f;
	float turnrange = 80;
	float offset;
	float sign = 0;
	public Base base;
	
	{
		material.maxvelocity = 2f;
		material.getRectangle().setSize(10);
		health = 10;
		offset = MathUtils.random(-30,30);
		sign = MathUtils.randomSign();
		speed += MathUtils.random(0.3f);
	}
	
	@Override
	public void Draw(){
		renderer.layer("drone", x, y).setRotation(velocity.angle() - 90).addShadow();
	}
	
	public void deathEvent(){
		super.deathEvent();
		if(base != null) base.spawned --;
		new BreakEffect("drone", velocity, 1f).setPosition(x, y).SendSelf();
	}

	@Override
	public void behaviorUpdate(){
		if(target == null) return;
		Vector2 add = new Vector2(WorldUtils.relative3(target.x, x), WorldUtils.relative3(target.y, y));
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
