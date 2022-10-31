package net.lifecity.mc.skillmaster.utils

import com.github.syari.spigot.api.particle.spawnParticle
import com.github.syari.spigot.api.scheduler.runTaskTimer
import net.lifecity.mc.skillmaster.SkillMaster
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.util.Vector
import kotlin.math.*

class DrawParticle {

    /**
     * 指定した位置にパーティクルを一つだけ表示するメソッド
     *
     * @param origin:   描画するLocation
     * @param point: パーティクルの位置ベクトル
     * @param particle: パーティクルの種類
     * @param data      :    パーティクルのデータ
     * @param rotX:     X軸回りに回転する角度
     * @param rotY:     Y軸回りに回転する角度
     * @param rotZ:     Z軸回りに回転する角度
     * @param count: パーティクルの表示数
     * @param speed:    パーティクルのスピード
     */
    private fun spawnParticle(
        origin: Location,
        particle: Particle,
        point: Vector,
        data: Any? = null,
        rotX: Double = 0.0,
        rotY: Double = 0.0,
        rotZ: Double = 0.0,
        count: Int = 1,
        speed: Double = 1.0
    ) {
        rotX(point, rotX)
        rotY(point, rotY)
        rotZ(point, rotZ)
        origin.add(point)
        // spawn something at origin
        origin.spawnParticle(particle, count, speed = speed, data = data)
        origin.subtract(point)
    }

    /**
     * 指定したパーティクルで弧を描画するメソッド
     *
     * @param origin:   描画するLocation
     * @param particle: パーティクルの種類
     * @param data      :    パーティクルのデータ
     * @param radius:   円の半径
     * @param points:   図形を成す点の数
     * @param rotX:     X軸回りに回転する角度
     * @param rotY:     Y軸回りに回転する角度
     * @param rotZ:     Z軸回りに回転する角度
     */
    fun drawArc(
        origin: Location,
        particle: Particle = Particle.SPELL_INSTANT,
        data: Any? = null,
        cycle: Double,
        radius: Double,
        points: Int,
        rotX: Double = 0.0,
        rotY: Double = 0.0,
        rotZ: Double = 0.0,
        count: Int = 1,
        speed: Double = 1.0
    ) {
        for (i in 0 until points) {
            val t = i * cycle / points
            val point = Vector(radius * cos(t), 0.0, radius * sin(t))
            spawnParticle(origin, particle, point, data, rotX, rotY, rotZ, count, speed)
        }
    }

    /**
     * 指定したパーティクルでアニメーションする弧を描画するメソッド
     *
     * @param origin:   描画するLocation
     * @param particle: パーティクルの種類
     * @param data      :    パーティクルのデータ
     * @param radius:   円の半径
     * @param points:   図形を成す点の数
     * @param rotX:     X軸回りに回転する角度
     * @param rotY:     Y軸回りに回転する角度
     * @param rotZ:     Z軸回りに回転する角度
     */
    fun drawAnimArc(
        origin: Location,
        particle: Particle = Particle.SPELL_INSTANT,
        data: Any? = null,
        cycle: Double,
        radius: Double,
        points: Int,
        rotX: Double = 0.0,
        rotY: Double = 0.0,
        rotZ: Double = 0.0,
        count: Int = 1,
        speed: Double = 1.0
    ) {
        var i = 0

        SkillMaster.INSTANCE.runTaskTimer(1) {
            if (i < points) {
                val t = i * cycle / points
                val point = Vector(radius * cos(t), 0.0, radius * sin(t))
                spawnParticle(origin, particle, point, data, rotX, rotY, rotZ, count, speed)
            } else {
                cancel()
            }
            i++
        }
    }

    /**
     * 指定したパーティクルで円を描画するメソッド
     *
     * @param origin:   描画するLocation
     * @param particle: パーティクルの種類
     * @param data      :    パーティクルのデータ
     * @param radius:   円の半径
     * @param points:   図形を成す点の数
     * @param rotX:     X軸回りに回転する角度
     * @param rotY:     Y軸回りに回転する角度
     * @param rotZ:     Z軸回りに回転する角度
     */
    fun drawCircle(
        origin: Location,
        particle: Particle = Particle.SPELL_INSTANT,
        data: Any? = null,
        radius: Double,
        points: Int,
        rotX: Double = 0.0,
        rotY: Double = 0.0,
        rotZ: Double = 0.0,
        count: Int = 1,
        speed: Double = 1.0
    )  = drawArc(origin, particle, data, 2 * PI, radius, points, rotX, rotY, rotZ, count, speed)

    /**
     * 指定したパーティクルでアニメーションする円を描画するメソッド
     *
     * @param origin:   描画するLocation
     * @param particle: パーティクルの種類
     * @param data      :    パーティクルのデータ
     * @param radius:   円の半径
     * @param points:   図形を成す点の数
     * @param rotX:     X軸回りに回転する角度
     * @param rotY:     Y軸回りに回転する角度
     * @param rotZ:     Z軸回りに回転する角度
     */
    fun drawAnimCircle(
        origin: Location,
        particle: Particle = Particle.SPELL_INSTANT,
        data: Any? = null,
        radius: Double,
        points: Int,
        rotX: Double = 0.0,
        rotY: Double = 0.0,
        rotZ: Double = 0.0,
        count: Int = 1,
        speed: Double = 1.0
    ) = drawAnimArc(origin, particle, data, 2 * PI, radius, points, rotX, rotY, rotZ, count, speed)

    /**
     * 指定したパーティクルで半円を描画するメソッド
     *
     * @param origin:   描画するLocation
     * @param particle: パーティクルの種類
     * @param data      :    パーティクルのデータ
     * @param radius:   円の半径
     * @param points:   図形を成す点の数
     * @param rotX:     X軸回りに回転する角度
     * @param rotY:     Y軸回りに回転する角度
     * @param rotZ:     Z軸回りに回転する角度
     */
    fun drawSemiCircle(
        origin: Location,
        particle: Particle = Particle.SPELL_INSTANT,
        data: Any? = null,
        radius: Double,
        points: Int,
        rotX: Double = 0.0,
        rotY: Double = 0.0,
        rotZ: Double = 0.0,
        count: Int = 1,
        speed: Double = 1.0
    ) = drawArc(origin, particle, data, PI,  radius, points, rotX, rotY, rotZ, count, speed)

    /**
     * 指定したパーティクルでアニメーションする半円を描画するメソッド
     *
     * @param origin:   描画するLocation
     * @param particle: パーティクルの種類
     * @param data      :    パーティクルのデータ
     * @param radius:   円の半径
     * @param points:   図形を成す点の数
     * @param rotX:     X軸回りに回転する角度
     * @param rotY:     Y軸回りに回転する角度
     * @param rotZ:     Z軸回りに回転する角度
     */
    fun drawAnimSemiCircle(
        origin: Location,
        particle: Particle = Particle.SPELL_INSTANT,
        data: Any? = null,
        radius: Double,
        points: Int,
        rotX: Double = 0.0,
        rotY: Double = 0.0,
        rotZ: Double = 0.0,
        count: Int = 1,
        speed: Double = 1.0
    ) = drawAnimArc(origin, particle, data, PI, radius, points, rotX, rotY, rotZ, count, speed)


    /**
     * 斬撃のパーティクルを表示する
     */
    fun drawSlash(
        origin: Location,
        particle: Particle,
        data: Any? = null,
        points: Int,
        rotX: Double = 0.0,
        rotY: Double = 0.0,
        rotZ: Double = 0.0,
        count: Int = 1,
        speed: Double = 1.0
    ) {
        var i = 0
        SkillMaster.INSTANCE.runTaskTimer(1) {
            if (i < points) {
                val t = i * 2 * PI / points
                val point = Vector(0.0, t, 0.0)
                spawnParticle(origin, particle, point, data, rotX, rotY, rotZ, count, speed)
            } else {
                cancel()
            }
            i++
        }
    }



    /**
     * 指定したパーティクルで渦巻を描画するメソッド
     *
     * @param origin:   描画するLocation
     * @param particle: パーティクルの種類
     * @param data      :    パーティクルのデータ
     * @param radius:   渦巻の真ん中の半径
     * @param points:   図形を成す点の数
     * @param rotX:     X軸回りに回転する角度
     * @param rotY:     Y軸回りに回転する角度
     * @param rotZ:     Z軸回りに回転する角度
     */
    fun drawSpiral(
        origin: Location,
        particle: Particle = Particle.SPELL_INSTANT,
        data: Any? = null,
        radius: Double,
        points: Int,
        rotX: Double = 0.0,
        rotY: Double = 0.0,
        rotZ: Double = 0.0,
        count: Int = 1,
        speed: Double = 1.0
    ) {
        for (i in 0 until points) {
            val t = i * 8 * PI / points
            val point = Vector(radius * t * sin(t), 0.0, radius * t * cos(t))
            spawnParticle(origin, particle, point, data, rotX, rotY, rotZ, count, speed)
        }
    }

    /**
     * 指定したパーティクルでアニメーションする渦巻を描画するメソッド
     *
     * @param origin:   描画するLocation
     * @param particle: パーティクルの種類
     * @param data      :    パーティクルのデータ
     * @param radius:   渦巻の真ん中の半径
     * @param points:   図形を成す点の数
     * @param rotX:     X軸回りに回転する角度
     * @param rotY:     Y軸回りに回転する角度
     * @param rotZ:     Z軸回りに回転する角度
     */
    fun drawAnimSpiral(
        origin: Location,
        particle: Particle = Particle.SPELL_INSTANT,
        data: Any? = null,
        radius: Double,
        points: Int,
        rotX: Double = 0.0,
        rotY: Double = 0.0,
        rotZ: Double = 0.0,
        count: Int = 1,
        speed: Double = 1.0
    ) {
        var i = 0

        SkillMaster.INSTANCE.runTaskTimer(1) {
            if (i < points) {
                val t = i * 8 * PI / points
                val point = Vector(radius * t * sin(t), 0.0, radius * t * cos(t))
                spawnParticle(origin, particle, point, data, rotX, rotY, rotZ, count, speed)
            } else {
                cancel()
            }
            i++
        }
    }

    /**
     * 指定したパーティクルで円錐形の渦巻を描画するメソッド
     *
     * @param origin:   描画するLocation
     * @param particle: パーティクルの種類
     * @param data      :    パーティクルのデータ
     * @param radius:   渦巻の真ん中の半径
     * @param points:   図形を成す点の数
     * @param rotX:     X軸回りに回転する角度
     * @param rotY:     Y軸回りに回転する角度
     * @param rotZ:     Z軸回りに回転する角度
     */
    fun drawConicSpiral(
        origin: Location,
        particle: Particle = Particle.SPELL_INSTANT,
        data: Any? = null,
        radius: Double,
        points: Int,
        rotX: Double = 0.0,
        rotY: Double = 0.0,
        rotZ: Double = 0.0,
        count: Int = 1,
        speed: Double = 1.0
    ) {
        for (i in 0 until points) {
            val t = i * 8 * PI / points
            val point = Vector(radius * t * sin(t), radius * t, radius * t * cos(t))
            spawnParticle(origin, particle, point, data, rotX, rotY, rotZ, count, speed)
        }
    }

    /**
     * 指定したパーティクルでアニメーションする円錐形の渦巻を描画するメソッド
     *
     * @param origin:   描画するLocation
     * @param particle: パーティクルの種類
     * @param data      :    パーティクルのデータ
     * @param radius:   渦巻の真ん中の半径
     * @param points:   図形を成す点の数
     * @param rotX:     X軸回りに回転する角度
     * @param rotY:     Y軸回りに回転する角度
     * @param rotZ:     Z軸回りに回転する角度
     */
    fun drawAnimConicSpiral(
        origin: Location,
        particle: Particle = Particle.SPELL_INSTANT,
        data: Any? = null,
        radius: Double,
        points: Int,
        rotX: Double = 0.0,
        rotY: Double = 0.0,
        rotZ: Double = 0.0,
        count: Int = 1,
        speed: Double = 1.0
    ) {
        var i = 0
        SkillMaster.INSTANCE.runTaskTimer(1) {
            if (i < points) {
                val t = i * 8 * PI / points
                val point = Vector(radius * t * sin(t), radius * t, radius * t * cos(t))
                spawnParticle(origin, particle, point, data, rotX, rotY, rotZ, count, speed)
            } else {
                cancel()
            }
            i++
        }
    }

    /**
     * 指定したパーティクルで円柱形の渦巻を描画するメソッド
     *
     * @param origin:   描画するLocation
     * @param particle: パーティクルの種類
     * @param data      :    パーティクルのデータ
     * @param radius:   渦巻の真ん中の半径
     * @param points:   図形を成す点の数
     * @param rotX:     X軸回りに回転する角度
     * @param rotY:     Y軸回りに回転する角度
     * @param rotZ:     Z軸回りに回転する角度
     */
    fun drawCylinderSpiral(
        origin: Location,
        particle: Particle = Particle.SPELL_INSTANT,
        data: Any? = null,
        radius: Double,
        points: Int,
        rotX: Double = 0.0,
        rotY: Double = 0.0,
        rotZ: Double = 0.0,
        count: Int = 1,
        speed: Double = 1.0
    ) {
        for (i in 0 until points) {
            val t = i * 8 * PI / points
            val point = Vector(radius * sin(t), radius * t, radius * cos(t))
            spawnParticle(origin, particle, point, data, rotX, rotY, rotZ, count, speed)
        }
    }

    /**
     * 指定したパーティクルでアニメーションする円柱形の渦巻を描画するメソッド
     *
     * @param origin:   描画するLocation
     * @param particle: パーティクルの種類
     * @param data      :    パーティクルのデータ
     * @param radius:   渦巻の真ん中の半径
     * @param points:   図形を成す点の数
     * @param rotX:     X軸回りに回転する角度
     * @param rotY:     Y軸回りに回転する角度
     * @param rotZ:     Z軸回りに回転する角度
     */
    fun drawAnimCyliderSpiral(
        origin: Location,
        particle: Particle = Particle.SPELL_INSTANT,
        data: Any? = null,
        radius: Double,
        points: Int,
        rotX: Double = 0.0,
        rotY: Double = 0.0,
        rotZ: Double = 0.0,
        count: Int = 1,
        speed: Double = 1.0
    ) {
        var i = 0
        SkillMaster.INSTANCE.runTaskTimer(1) {
            if (i < points) {
                val t = i * 8 * PI / points
                val point = Vector(radius * sin(t), radius * t, radius * cos(t))
                spawnParticle(origin, particle, point, data, rotX, rotY, rotZ, count, speed)

            } else {
                cancel()
            }
            i++
        }
    }

    /**
     * 指定したパーティクルで渦巻からなる球形を描画するメソッド
     *
     * @param origin:   描画するLocation
     * @param particle: パーティクルの種類
     * @param data      :    パーティクルのデータ
     * @param radius:   球の半径
     * @param points:   図形を成す点の数
     * @param rotX:     X軸回りに回転する角度
     * @param rotY:     Y軸回りに回転する角度
     * @param rotZ:     Z軸回りに回転する角度
     */
    fun drawSpiralSphere(
        origin: Location,
        particle: Particle = Particle.SPELL_INSTANT,
        data: Any? = null,
        radius: Double,
        points: Int,
        rotX: Double = 0.0,
        rotY: Double = 0.0,
        rotZ: Double = 0.0,
        count: Int = 1,
        speed: Double = 1.0
    ) {
        for (i in 0 until points) {
            val t = i * 8 * PI / points
            val point = Vector(
                radius * sin(t / 10 * PI),
                radius * sin(t % 10 * PI) * cos(t / 10 * PI),
                radius * cos(t % 10 * PI) * cos(t / 10 * PI)
            )
            spawnParticle(origin, particle, point, data, rotX, rotY, rotZ, count, speed)

        }
    }

    /**
     * 指定したパーティクルでアニメーションする渦巻からなる球形を描画するメソッド
     *
     * @param origin:   描画するLocation
     * @param particle: パーティクルの種類
     * @param data      :    パーティクルのデータ
     * @param radius:   球の半径
     * @param points:   図形を成す点の数
     * @param rotX:     X軸回りに回転する角度
     * @param rotY:     Y軸回りに回転する角度
     * @param rotZ:     Z軸回りに回転する角度
     */
    fun drawAnimSpiralSphere(
        origin: Location,
        particle: Particle = Particle.SPELL_INSTANT,
        data: Any? = null,
        radius: Double,
        points: Int,
        rotX: Double = 0.0,
        rotY: Double = 0.0,
        rotZ: Double = 0.0,
        count: Int = 1,
        speed: Double = 1.0
    ) {
        var i = 0

        SkillMaster.INSTANCE.runTaskTimer(1) {
            if (i < points) {
                val t = i * 8 * PI / points
                val point = Vector(
                    radius * sin(t / 10 * PI),
                    radius * sin(t % 10 * PI) * cos(t / 10 * PI),
                    radius * cos(t % 10 * PI) * cos(t / 10 * PI)
                )
                spawnParticle(origin, particle, point, data, rotX, rotY, rotZ, count, speed)

            } else {
                cancel()
            }
            i++
        }

    }

    /**
     * 指定したパーティクルでアステロイドを描画するメソッド
     *
     * @param origin:   描画するLocation
     * @param particle: パーティクルの種類
     * @param data      :    パーティクルのデータ
     * @param radius:   アステロイドの半径
     * @param points:   図形を成す点の数
     * @param rotX:     X軸回りに回転する角度
     * @param rotY:     Y軸回りに回転する角度
     * @param rotZ:     Z軸回りに回転する角度
     */
    fun drawAsteroid(
        origin: Location,
        particle: Particle = Particle.SPELL_INSTANT,
        data: Any? = null,
        radius: Double,
        points: Int,
        rotX: Double = 0.0,
        rotY: Double = 0.0,
        rotZ: Double = 0.0,
        count: Int = 1,
        speed: Double = 1.0
    ) {
        for (i in 0 until points) {
            val t = i * 8 * PI / points
            val point = Vector(
                radius * cos(t).pow(3.0),
                0.0,
                radius * sin(t).pow(3.0)
            )
            spawnParticle(origin, particle, point, data, rotX, rotY, rotZ, count, speed)

        }
    }

    /**
     * 指定したパーティクルでアニメーションするアステロイドを描画するメソッド
     *
     * @param origin:   描画するLocation
     * @param particle: パーティクルの種類
     * @param data      :    パーティクルのデータ
     * @param radius:   アステロイドの半径
     * @param points:   図形を成す点の数
     * @param rotX:     X軸回りに回転する角度
     * @param rotY:     Y軸回りに回転する角度
     * @param rotZ:     Z軸回りに回転する角度
     */
    fun drawAnimAsteroid(
        origin: Location,
        particle: Particle = Particle.SPELL_INSTANT,
        data: Any? = null,
        radius: Double,
        points: Int,
        rotX: Double = 0.0,
        rotY: Double = 0.0,
        rotZ: Double = 0.0,
        count: Int = 1,
        speed: Double = 1.0
    ) {
        var i = 0

        SkillMaster.INSTANCE.runTaskTimer(1) {
            if (i < points) {
                val t = i * 8 * PI / points
                val point = Vector(
                    radius * cos(t).pow(3.0),
                    0.0,
                    radius * sin(t).pow(3.0)
                )
                spawnParticle(origin, particle, point, data, rotX, rotY, rotZ, count, speed)

            } else {
                cancel()
            }
            i++
        }
    }

    /**
     * 指定したパーティクルでハートを描画するメソッド
     *
     * @param origin:   描画するLocation
     * @param particle: パーティクルの種類
     * @param data      :    パーティクルのデータ
     * @param size:     大きさ（１がデフォルト）
     * @param points:   図形を成す点の数
     * @param rotX:     X軸回りに回転する角度
     * @param rotY:     Y軸回りに回転する角度
     * @param rotZ:     Z軸回りに回転する角度
     */
    fun drawHeart(
        origin: Location,
        particle: Particle = Particle.SPELL_INSTANT,
        data: Any? = null,
        size: Double,
        points: Int,
        rotX: Double = 0.0,
        rotY: Double = 0.0,
        rotZ: Double = 0.0,
        count: Int = 1,
        speed: Double = 1.0
    ) {
        for (i in 0 until points) {
            val t = i * 2 * PI / points
            val point = Vector(
                size * (16 * sin(t).pow(3.0)) / 3,
                0.0,
                size * (13 * cos(t) - 5 * cos(2 * t) - 2 * cos(3 * t) - cos(4 * t)) / 3
            )
            spawnParticle(origin, particle, point, data, rotX, rotY, rotZ, count, speed)

        }
    }

    /**
     * 指定したパーティクルでアニメーションするハートを描画するメソッド
     *
     * @param origin:   描画するLocation
     * @param particle: パーティクルの種類
     * @param data      :    パーティクルのデータ
     * @param size:     大きさ（１がデフォルト）
     * @param points:   図形を成す点の数
     * @param rotX:     X軸回りに回転する角度
     * @param rotY:     Y軸回りに回転する角度
     * @param rotZ:     Z軸回りに回転する角度
     */
    fun drawAnimHeart(
        origin: Location,
        particle: Particle = Particle.SPELL_INSTANT,
        data: Any? = null,
        size: Double,
        points: Int,
        rotX: Double = 0.0,
        rotY: Double = 0.0,
        rotZ: Double = 0.0,
        count: Int = 1,
        speed: Double = 1.0
    ) {
        var i = 0

        SkillMaster.INSTANCE.runTaskTimer(1) {
            if (i < points) {
                val t = i * 2 * PI / points
                val point = Vector(
                    size * (16 * sin(t).pow(3.0)) / 3,
                    0.0,
                    size * (13 * cos(t) - 5 * cos(2 * t) - 2 * cos(3 * t) - cos(4 * t)) / 3
                )
                spawnParticle(origin, particle, point, data, rotX, rotY, rotZ, count, speed)

            } else {
                cancel()
            }
            i++
        }
    }

    /**
     * 指定したパーティクルでリサージュ曲線を描画するメソッド
     * 数式は以下の通り
     * x = Asin(at + δ)
     * y = Bsin(bt)
     *
     * @param origin:   描画するLocation
     * @param particle: パーティクルの種類
     * @param data      :    パーティクルのデータ
     * @param A:        パラメータA
     * @param B:        パラメータB
     * @param a:        パラメータa
     * @param b:        パラメータb
     * @param delta:    パラメータδ
     * @param points:   図形を表す点の数
     * @param rotX:     X軸回りに回転する角度
     * @param rotY:     Y軸回りに回転する角度
     * @param rotZ:     Z軸回りに回転する角度
     */
    fun drawLissajous(
        origin: Location,
        particle: Particle = Particle.SPELL_INSTANT,
        data: Any? = null,
        A: Double,
        B: Double,
        a: Double,
        b: Double,
        delta: Double,
        points: Int,
        rotX: Double = 0.0,
        rotY: Double = 0.0,
        rotZ: Double = 0.0,
        count: Int = 1,
        speed: Double = 1.0
    ) {
        for (i in 0 until points) {
            val t = i * 2 * PI / points
            val point = Vector(
                A * sin(a * t + delta),
                0.0,
                B * sin(b * t)
            )
            spawnParticle(origin, particle, point, data, rotX, rotY, rotZ, count, speed)

        }
    }

    /**
     * 指定したパーティクルでアニメーションするリサージュ曲線を描画するメソッド
     * 数式は以下の通り
     * x = Asin(at + δ)
     * y = Bsin(bt)
     *
     * @param origin:   描画するLocation
     * @param particle: パーティクルの種類
     * @param data      :    パーティクルのデータ
     * @param A:        パラメータA
     * @param B:        パラメータB
     * @param a:        パラメータa
     * @param b:        パラメータb
     * @param delta:    パラメータδ
     * @param points:   図形を表す点の数
     * @param rotX:     X軸回りに回転する角度
     * @param rotY:     Y軸回りに回転する角度
     * @param rotZ:     Z軸回りに回転する角度
     */
    fun drawAnimLissajous(
        origin: Location,
        particle: Particle = Particle.SPELL_INSTANT,
        data: Any? = null,
        A: Double,
        B: Double,
        a: Double,
        b: Double,
        delta: Double,
        points: Int,
        rotX: Double = 0.0,
        rotY: Double = 0.0,
        rotZ: Double = 0.0,
        count: Int = 1,
        speed: Double = 1.0
    ) {
        var i = 0

        SkillMaster.INSTANCE.runTaskTimer(1) {
            if (i < points) {
                val t = i * 2 * PI / points
                val point = Vector(
                    A * sin(a * t + delta),
                    0.0,
                    B * sin(b * t)
                )
                spawnParticle(origin, particle, point, data, rotX, rotY, rotZ, count, speed)

            } else {
                cancel()
            }
            i++
        }
    }

    /**
     * 指定したパーティクルでリマソン曲線を描画するメソッド
     *
     * @param origin:   描画するLocation
     * @param particle: パーティクルの種類
     * @param data      :    パーティクルのデータ
     * @param size:     大きさ（１がデフォルト）
     * @param points:   図形を成す点の数
     * @param rotX:     X軸回りに回転する角度
     * @param rotY:     Y軸回りに回転する角度
     * @param rotZ:     Z軸回りに回転する角度
     */
    fun drawLimason(
        origin: Location,
        particle: Particle = Particle.SPELL_INSTANT,
        data: Any? = null,
        size: Double,
        points: Int,
        rotX: Double = 0.0,
        rotY: Double = 0.0,
        rotZ: Double = 0.0,
        count: Int = 1,
        speed: Double = 1.0
    ) {
        for (i in 0 until points) {
            val t = i * 2 * PI / points
            val point = Vector(
                size * (1 + 2 * cos(t)) * cos(t),
                0.0,
                size * (1 + 2 * cos(t)) * sin(t)
            )
            spawnParticle(origin, particle, point, data, rotX, rotY, rotZ, count, speed)

        }
    }

    /**
     * 指定したパーティクルでアニメーションするリマソン曲線を描画するメソッド
     *
     * @param origin:   描画するLocation
     * @param particle: パーティクルの種類
     * @param data      :    パーティクルのデータ
     * @param size:     大きさ（１がデフォルト）
     * @param points:   図形を成す点の数
     * @param rotX:     X軸回りに回転する角度
     * @param rotY:     Y軸回りに回転する角度
     * @param rotZ:     Z軸回りに回転する角度
     */
    fun drawAnimLimason(
        origin: Location,
        particle: Particle = Particle.SPELL_INSTANT,
        data: Any? = null,
        size: Double,
        points: Int,
        rotX: Double = 0.0,
        rotY: Double = 0.0,
        rotZ: Double = 0.0,
        count: Int = 1,
        speed: Double = 1.0
    ) {
        var i = 0

        SkillMaster.INSTANCE.runTaskTimer(1) {
            if (i < points) {
                val t = i * 2 * PI / points
                val point = Vector(
                    size * (1 + 2 * cos(t)) * cos(t),
                    0.0,
                    size * (1 + 2 * cos(t)) * sin(t)
                )
                spawnParticle(origin, particle, point, data, rotX, rotY, rotZ, count, speed)

            } else {
                cancel()
            }
            i++
        }
    }

    /**
     * 指定したパーティクルでサイクロイドを描画するメソッド
     *
     * @param origin:    描画するLocation
     * @param particle:  パーティクルの種類
     * @param data       :    パーティクルのデータ
     * @param a:         一回転の長さ
     * @param scrollNum: 回転回数
     * @param points:    図形を成す点の数
     * @param rotX:      X軸回りに回転する角度
     * @param rotY:      Y軸回りに回転する角度
     * @param rotZ:      Z軸回りに回転する角度
     */
    fun drawCycloid(
        origin: Location,
        particle: Particle = Particle.SPELL_INSTANT,
        data: Any? = null,
        a: Double,
        scrollNum: Double,
        points: Int,
        rotX: Double = 0.0,
        rotY: Double = 0.0,
        rotZ: Double = 0.0,
        count: Int = 1,
        speed: Double = 1.0
    ) {
        for (i in 0 until points) {
            val t = i * scrollNum * 2 * PI / points
            val point = Vector(
                a * (t - sin(t)),
                0.0,
                a * (1 - cos(t))
            )
            spawnParticle(origin, particle, point, data, rotX, rotY, rotZ, count, speed)

        }
    }

    /**
     * 指定したパーティクルでサイクロイドを描画するメソッド
     *
     * @param origin:    描画するLocation
     * @param particle:  パーティクルの種類
     * @param data       :    パーティクルのデータ
     * @param a:         一回転の長さ
     * @param scrollNum: 回転回数
     * @param points:    図形を成す点の数
     * @param rotX:      X軸回りに回転する角度
     * @param rotY:      Y軸回りに回転する角度
     * @param rotZ:      Z軸回りに回転する角度
     */
    fun drawAnimCycloid(
        origin: Location,
        particle: Particle = Particle.SPELL_INSTANT,
        data: Any? = null,
        a: Double,
        scrollNum: Double,
        points: Int,
        rotX: Double = 0.0,
        rotY: Double = 0.0,
        rotZ: Double = 0.0,
        count: Int = 1,
        speed: Double = 1.0
    ) {
        var i = 0

        SkillMaster.INSTANCE.runTaskTimer(1) {
            if (i < points) {
                val t = i * scrollNum * 2 * PI / points
                val point = Vector(
                    a * (t - sin(t)),
                    0.0,
                    a * (1 - cos(t))
                )
                spawnParticle(origin, particle, point, data, rotX, rotY, rotZ, count, speed)

            } else {
                cancel()
            }
            i++
        }
    }

    fun drawStar(
        origin: Location,
        particle: Particle = Particle.SPELL_INSTANT,
        data: Any? = null,
        a: Double,
        b: Double,
        c: Double,
        r0: Double,
        n: Int,
        points: Int,
        rotX: Double = 0.0,
        rotY: Double = 0.0,
        rotZ: Double = 0.0,
        count: Int = 1,
        speed: Double = 1.0
    ) {
        val xmax = sqrt(-(1 / b.pow(2.0)) * ln(2 * E.pow(-a.pow(2.0)) - 1))
        for (i in 0 until points) {
            val t = i * 2 * PI / points
            val r = r0 + 1 / c * sqrt(
                -ln(
                    2 * E.pow(-a.pow(2.0)) - E.pow(
                        -b.pow(2.0) * xmax.pow(2.0) * sin((t - PI / 2) * n / 2).pow(2.0)
                    )
                )
            )
            val point = Vector(
                r * cos(t),
                0.0,
                r * sin(t)
            )
            spawnParticle(origin, particle, point, data, rotX, rotY, rotZ, count, speed)

        }
    }

    /**
     * 与えたVectorをX軸回りでtだけ回転させる
     *
     * @param point: 回転させたいVector
     * @param t:     角度
     */
    private fun rotX(point: Vector, t: Double) {
        val y = point.y
        point.y = y * cos(t) - point.z * sin(t)
        point.z = y * sin(t) + point.z * cos(t)
    }

    /**
     * 与えたVectorをY軸回りでtだけ回転させる
     *
     * @param point: 回転させたいVector
     * @param t:     角度
     */
    private fun rotY(point: Vector, t: Double) {
        val z = point.z
        point.z = z * cos(t) - point.x * sin(t)
        point.x = z * sin(t) + point.x * cos(t)
    }

    /**
     * 与えたVectorをZ軸回りでtだけ回転させる
     *
     * @param point: 回転させたいVector
     * @param t:     角度
     */
    private fun rotZ(point: Vector, t: Double) {
        val x = point.x
        point.x = x * cos(t) - point.y * sin(t)
        point.y = x * sin(t) + point.y * cos(t)
    }
}