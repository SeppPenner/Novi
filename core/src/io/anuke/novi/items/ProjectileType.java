package io.anuke.novi.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import io.anuke.novi.effects.EffectType;
import io.anuke.novi.effects.Effects;
import io.anuke.novi.entities.Entities;
import io.anuke.novi.entities.basic.Bullet;
import io.anuke.novi.entities.basic.DamageArea;
import io.anuke.novi.graphics.Wrap;
import io.anuke.novi.utils.Timers;
import io.anuke.ucore.core.Draw;
import io.anuke.ucore.graphics.Hue;
import io.anuke.ucore.util.Angles;
import io.anuke.ucore.util.Mathf;

public enum ProjectileType{
	plasmabullet{
		
		public String drawName(){
			return "bullet";
		}
		
		public int getLifetime(){
			return 30;
		}
		
		public float getSpeed(){
			return 7;
		}
		
		public int damage(){
			return 0;
		}
		
		public void hitEvent(Bullet bullet){
			Effects.effect(hitEffect(), bullet.x, bullet.y, Color.valueOf("82f4a8ff"));
		}
	},
	redbullet{
		
		public String drawName(){
			return "dronebullet";
		}
		
		public int getLifetime(){
			return 60;
		}
		
		public float getSpeed(){
			return 3;
		}
		
		public int damage(){
			return 4;
		}
		
		public void hitEvent(Bullet bullet){
			Effects.effect(hitEffect(), bullet.x, bullet.y, Color.valueOf("ff4141ff"));
		}
	},
	explosivebullet{
		
		public int getLifetime(){
			return 100;
		}
		
		public float getSpeed(){
			return 3;
		}
		
		public void destroyEvent(Bullet bullet){
			
			Effects.effect(EffectType.shockwave, bullet.x, bullet.y);
			Effects.effect(EffectType.explosion, bullet.x, bullet.y);
			
			new DamageArea(30f, 16f).set(bullet.x, bullet.y).add();
			Effects.shake(20f, 10f, bullet.x, bullet.y);
		}
		
		public void setup(Bullet bullet){
			bullet.material.getRectangle().setSize(5f);
		}
		
		public int damage(){
			return 15;
		}
	},
	mine{
		public void draw(Bullet bullet){
			defaultDraw(bullet);
			
			Draw.color(Hue.mix(Color.valueOf("82f4a8"), Color.valueOf("20344f"), bullet.life()/getLifetime()));
			Wrap.rect("minecenter", bullet.x, bullet.y, bullet.velocity.angle() - 90);
			Draw.color();
		}
		
		public int getLifetime(){
			return 200;
		}
		
		public float getSpeed(){
			return 0;
		}
		
		public void destroyEvent(Bullet bullet){
			new DamageArea(30f, 16f).setDamage(15).set(bullet.x, bullet.y).add();
			Effects.effect(EffectType.explosion, bullet.x, bullet.y);
			Effects.shake(20f, 10f, bullet.x, bullet.y);
		}
		
		public void setup(Bullet bullet){
			bullet.material.set(10f);
		}
		
		public int damage(){
			return 5;
		}
		
		public boolean collideWithBases(){
			return false;
		}
	},
	laser{
		
		float length = 200f;
		
		public void setup(Bullet bullet){
			bullet.material.collide = false;
			bullet.material.set(5);
		}
		
		public int getLifetime(){
			return 999999999;
		}
		
		public float getSpeed(){
			return 0.01f;
		}
		
		public int damage(){
			return 1;
		}
		
		public void hitEvent(Bullet bullet){
			Effects.effect(hitEffect(), bullet.x, bullet.y, Color.valueOf("f5d06c"));
		}
		
		public void draw(Bullet bullet){
			Vector2 vec = Angles.translation(bullet.velocity.angle(), length-3);
			float rotation = bullet.velocity.angle();
			
			vec.setLength(11);
			
			float x = bullet.x + vec.x, y = bullet.y + vec.y;
			
			vec.setLength(length);
			
			float x2 = bullet.x + vec.x, y2 = bullet.y + vec.y;
			
			Draw.colorl(Mathf.clamp(0.75f + MathUtils.random(0.2f) + Math.abs(MathUtils.sin(Timers.time()/3f)/4f)));
			Wrap.laser("laser", "laserend", x, y, x2, y2, rotation);
			Draw.color();
		}
		
		public void update(Bullet bullet){
			Vector2 v = Angles.translation(bullet.velocity.angle() + 90, length);
			
			Entities.spatial().raycastBullet(bullet.x, bullet.y, bullet.x + v.x, bullet.y + v.y, bullet);
			
		}
		
		public boolean destroyOnHit(){
			return false;
		}
		
		public boolean followParent(){
			return true;
		}
	};
	
	public void update(Bullet bullet){
		
	}
	
	public void setup(Bullet bullet){
		
	}
	
	public boolean destroyOnHit(){
		return true;
	}
	
	public float getSpeed(){
		return 4;
	}
	
	public int getLifetime(){
		return 100;
	}
	
	public int damage(){
		return 1;
	}
	
	public boolean collideWithOtherProjectiles(){
		return false;
	}
	
	public boolean collide(){
		return true;
	}
	
	public boolean collideWithBases(){
		return true;
	}
	
	public boolean followParent(){
		return false;
	}
	
	public String drawName(){
		return name();
	}
	
	public Color trailColor(){
		return Color.WHITE;
	}
	
	public EffectType hitEffect(){
		return EffectType.hit;
	}
	
	public void hitEvent(Bullet bullet){
		Effects.effect(hitEffect(), bullet.x, bullet.y);
	}
	
	public void destroyEvent(Bullet bullet){
		
	}
	
	public void defaultDraw(Bullet bullet){
		Wrap.rect(drawName(), bullet.x, bullet.y, bullet.velocity.angle() - 90);
	}
	
	public void draw(Bullet bullet){
		Wrap.rect(drawName(), bullet.x, bullet.y, bullet.velocity.angle() - 90);
	}
}
