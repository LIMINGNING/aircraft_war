package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.prop.FireProp;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class HeroAircraftTest {

    @Test
    void crash() {
        // 1. 英雄机初始化，指定坐标(100, 100)
        HeroAircraft hero = HeroAircraft.getHeroAircraft();
        hero.setLocation(100, 100);

        // 2. 创建火力道具，指定坐标(100, 100)
        FireProp fireProp = new FireProp(100, 100, 0, 0);

        // 3. 调用英雄机的crash方法，传入火力道具
        boolean result = hero.crash(fireProp);

        // 4. 判断是否碰撞
        assertTrue(result);
    }

    @Test
    void shoot() {
        HeroAircraft hero = HeroAircraft.getHeroAircraft();
        hero.setLocation(200, 300);
        List<BaseBullet> bullets = hero.shoot();
        assertFalse(bullets.isEmpty());
        // 检查第一个子弹的x坐标与英雄机一致
        assertEquals(200, bullets.get(0).getLocationX());
    }

    @Test
    void decreaseHp() {
        HeroAircraft hero = HeroAircraft.getHeroAircraft();
        hero.decreaseHp(30);
        assertEquals(70, hero.getHp());
    }
}