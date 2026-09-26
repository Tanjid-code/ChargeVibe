package com.tanjid.chargevibe.icons

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

data class CuteIconInfo(
    val id: Int,
    val name: String,
    val category: String
)

object CuteIconGenerator {

    val ALL_ICONS: List<CuteIconInfo> = listOf(
        // Animals (1-12)
        CuteIconInfo(1, "Pink Bunny", "Animals"),
        CuteIconInfo(2, "Kitty Cat", "Animals"),
        CuteIconInfo(3, "Teddy Bear", "Animals"),
        CuteIconInfo(4, "Baby Panda", "Animals"),
        CuteIconInfo(5, "Little Duck", "Animals"),
        CuteIconInfo(6, "Shiba Inu", "Animals"),
        CuteIconInfo(7, "Baby Penguin", "Animals"),
        CuteIconInfo(8, "Unicorn", "Animals"),
        CuteIconInfo(9, "Hamster", "Animals"),
        CuteIconInfo(10, "Pink Piggy", "Animals"),
        CuteIconInfo(11, "Baby Seal", "Animals"),
        CuteIconInfo(12, "Sleeping Fox", "Animals"),

        // Battery Characters (13-24)
        CuteIconInfo(13, "Heart Battery", "Battery"),
        CuteIconInfo(14, "Bunny Battery", "Battery"),
        CuteIconInfo(15, "Star Battery", "Battery"),
        CuteIconInfo(16, "Cat Battery", "Battery"),
        CuteIconInfo(17, "Peach Battery", "Battery"),
        CuteIconInfo(18, "Cloud Battery", "Battery"),
        CuteIconInfo(19, "Boba Battery", "Battery"),
        CuteIconInfo(20, "Strawberry Battery", "Battery"),
        CuteIconInfo(21, "Flower Battery", "Battery"),
        CuteIconInfo(22, "Moon Battery", "Battery"),
        CuteIconInfo(23, "Rainbow Battery", "Battery"),
        CuteIconInfo(24, "Diamond Battery", "Battery"),

        // Fashion & Beauty (25-36)
        CuteIconInfo(25, "Pink Bow", "Fashion"),
        CuteIconInfo(26, "Lipstick", "Fashion"),
        CuteIconInfo(27, "High Heel", "Fashion"),
        CuteIconInfo(28, "Princess Crown", "Fashion"),
        CuteIconInfo(29, "Handbag", "Fashion"),
        CuteIconInfo(30, "Perfume", "Fashion"),
        CuteIconInfo(31, "Nail Polish", "Fashion"),
        CuteIconInfo(32, "Diamond Ring", "Fashion"),
        CuteIconInfo(33, "Sunglasses", "Fashion"),
        CuteIconInfo(34, "Pink Dress", "Fashion"),
        CuteIconInfo(35, "Pearl Necklace", "Fashion"),
        CuteIconInfo(36, "Hair Ribbon", "Fashion"),

        // Food & Drinks (37-48)
        CuteIconInfo(37, "Boba Tea", "Food"),
        CuteIconInfo(38, "Cupcake", "Food"),
        CuteIconInfo(39, "Donut", "Food"),
        CuteIconInfo(40, "Ice Cream", "Food"),
        CuteIconInfo(41, "Strawberry", "Food"),
        CuteIconInfo(42, "Cherry", "Food"),
        CuteIconInfo(43, "Lollipop", "Food"),
        CuteIconInfo(44, "Macaron", "Food"),
        CuteIconInfo(45, "Cookie", "Food"),
        CuteIconInfo(46, "Watermelon", "Food"),
        CuteIconInfo(47, "Pancake", "Food"),
        CuteIconInfo(48, "Coffee Cup", "Food"),

        // Magic & Hearts (49-60)
        CuteIconInfo(49, "Beating Heart", "Hearts"),
        CuteIconInfo(50, "Sparkle Star", "Hearts"),
        CuteIconInfo(51, "Magic Wand", "Hearts"),
        CuteIconInfo(52, "Sakura Flower", "Hearts"),
        CuteIconInfo(53, "Butterfly", "Hearts"),
        CuteIconInfo(54, "Rainbow", "Hearts"),
        CuteIconInfo(55, "Crescent Moon", "Hearts"),
        CuteIconInfo(56, "Crystal Ball", "Hearts"),
        CuteIconInfo(57, "Music Note", "Hearts"),
        CuteIconInfo(58, "Love Letter", "Hearts"),
        CuteIconInfo(59, "Heart Balloon", "Hearts"),
        CuteIconInfo(60, "Lucky Clover", "Hearts")
    )

    val CATEGORIES = listOf("Animals", "Battery", "Fashion", "Food", "Hearts")

    fun generateIcon(context: Context, iconId: Int, sizePx: Int): Drawable {
        val bitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val s = sizePx.toFloat()
        val cx = s / 2f
        val cy = s / 2f
        val r = s * 0.4f

        when (iconId) {
            1 -> drawPinkBunny(canvas, s, cx, cy, r)
            2 -> drawKittyCat(canvas, s, cx, cy, r)
            3 -> drawTeddyBear(canvas, s, cx, cy, r)
            4 -> drawBabyPanda(canvas, s, cx, cy, r)
            5 -> drawLittleDuck(canvas, s, cx, cy, r)
            6 -> drawShibaInu(canvas, s, cx, cy, r)
            7 -> drawBabyPenguin(canvas, s, cx, cy, r)
            8 -> drawUnicorn(canvas, s, cx, cy, r)
            9 -> drawHamster(canvas, s, cx, cy, r)
            10 -> drawPinkPiggy(canvas, s, cx, cy, r)
            11 -> drawBabySeal(canvas, s, cx, cy, r)
            12 -> drawSleepingFox(canvas, s, cx, cy, r)
            13 -> drawHeartBattery(canvas, s, cx, cy, r)
            14 -> drawBunnyBattery(canvas, s, cx, cy, r)
            15 -> drawStarBattery(canvas, s, cx, cy, r)
            16 -> drawCatBattery(canvas, s, cx, cy, r)
            17 -> drawPeachBattery(canvas, s, cx, cy, r)
            18 -> drawCloudBattery(canvas, s, cx, cy, r)
            19 -> drawBobaBattery(canvas, s, cx, cy, r)
            20 -> drawStrawberryBattery(canvas, s, cx, cy, r)
            21 -> drawFlowerBattery(canvas, s, cx, cy, r)
            22 -> drawMoonBattery(canvas, s, cx, cy, r)
            23 -> drawRainbowBattery(canvas, s, cx, cy, r)
            24 -> drawDiamondBattery(canvas, s, cx, cy, r)
            25 -> drawPinkBow(canvas, s, cx, cy, r)
            26 -> drawLipstick(canvas, s, cx, cy, r)
            27 -> drawHighHeel(canvas, s, cx, cy, r)
            28 -> drawPrincessCrown(canvas, s, cx, cy, r)
            29 -> drawHandbag(canvas, s, cx, cy, r)
            30 -> drawPerfume(canvas, s, cx, cy, r)
            31 -> drawNailPolish(canvas, s, cx, cy, r)
            32 -> drawDiamondRing(canvas, s, cx, cy, r)
            33 -> drawSunglasses(canvas, s, cx, cy, r)
            34 -> drawPinkDress(canvas, s, cx, cy, r)
            35 -> drawPearlNecklace(canvas, s, cx, cy, r)
            36 -> drawHairRibbon(canvas, s, cx, cy, r)
            37 -> drawBobaTea(canvas, s, cx, cy, r)
            38 -> drawCupcake(canvas, s, cx, cy, r)
            39 -> drawDonut(canvas, s, cx, cy, r)
            40 -> drawIceCream(canvas, s, cx, cy, r)
            41 -> drawStrawberry(canvas, s, cx, cy, r)
            42 -> drawCherry(canvas, s, cx, cy, r)
            43 -> drawLollipop(canvas, s, cx, cy, r)
            44 -> drawMacaron(canvas, s, cx, cy, r)
            45 -> drawCookie(canvas, s, cx, cy, r)
            46 -> drawWatermelon(canvas, s, cx, cy, r)
            47 -> drawPancake(canvas, s, cx, cy, r)
            48 -> drawCoffeeCup(canvas, s, cx, cy, r)
            49 -> drawBeatingHeart(canvas, s, cx, cy, r)
            50 -> drawSparkleStar(canvas, s, cx, cy, r)
            51 -> drawMagicWand(canvas, s, cx, cy, r)
            52 -> drawSakuraFlower(canvas, s, cx, cy, r)
            53 -> drawButterfly(canvas, s, cx, cy, r)
            54 -> drawRainbow(canvas, s, cx, cy, r)
            55 -> drawCrescentMoon(canvas, s, cx, cy, r)
            56 -> drawCrystalBall(canvas, s, cx, cy, r)
            57 -> drawMusicNote(canvas, s, cx, cy, r)
            58 -> drawLoveLetter(canvas, s, cx, cy, r)
            59 -> drawHeartBalloon(canvas, s, cx, cy, r)
            60 -> drawLuckyClover(canvas, s, cx, cy, r)
            else -> drawBeatingHeart(canvas, s, cx, cy, r)
        }

        return BitmapDrawable(context.resources, bitmap)
    }

    // ════════════════════════════════════════
    // Helper Paint Creators
    // ════════════════════════════════════════
    private fun fill(color: Int): Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.color = color
        style = Paint.Style.FILL
    }

    private fun stroke(color: Int, width: Float): Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.color = color
        style = Paint.Style.STROKE
        strokeWidth = width
        strokeCap = Paint.Cap.ROUND
    }

    private fun drawFace(canvas: Canvas, cx: Float, cy: Float, s: Float, offset: Float = 0f) {
        val eyeR = s * 0.025f
        canvas.drawCircle(cx - s * 0.08f, cy - s * 0.02f + offset, eyeR, fill(Color.BLACK))
        canvas.drawCircle(cx + s * 0.08f, cy - s * 0.02f + offset, eyeR, fill(Color.BLACK))
        // tiny white eye highlight
        canvas.drawCircle(cx - s * 0.075f, cy - s * 0.025f + offset, eyeR * 0.4f, fill(Color.WHITE))
        canvas.drawCircle(cx + s * 0.085f, cy - s * 0.025f + offset, eyeR * 0.4f, fill(Color.WHITE))
    }

    private fun drawSmile(canvas: Canvas, cx: Float, cy: Float, s: Float, offset: Float = 0f) {
        val p = stroke(0xFFFF69B4.toInt(), s * 0.015f)
        val path = Path().apply {
            moveTo(cx - s * 0.04f, cy + s * 0.04f + offset)
            quadTo(cx, cy + s * 0.07f + offset, cx + s * 0.04f, cy + s * 0.04f + offset)
        }
        canvas.drawPath(path, p)
    }

    private fun drawBlush(canvas: Canvas, cx: Float, cy: Float, s: Float, r: Float, offset: Float = 0f) {
        val blushPaint = fill(0x40FFB6C1.toInt())
        canvas.drawCircle(cx - r * 0.7f, cy + s * 0.02f + offset, s * 0.04f, blushPaint)
        canvas.drawCircle(cx + r * 0.7f, cy + s * 0.02f + offset, s * 0.04f, blushPaint)
    }

    private fun drawHeart(canvas: Canvas, cx: Float, cy: Float, size: Float, color: Int) {
        val p = fill(color)
        val path = Path().apply {
            moveTo(cx, cy + size * 0.3f)
            cubicTo(cx - size * 0.5f, cy - size * 0.1f, cx - size * 0.5f, cy - size * 0.5f, cx, cy - size * 0.2f)
            cubicTo(cx + size * 0.5f, cy - size * 0.5f, cx + size * 0.5f, cy - size * 0.1f, cx, cy + size * 0.3f)
        }
        canvas.drawPath(path, p)
    }

    // ════════════════════════════════════════
    // CATEGORY 1: ANIMALS (1-12)
    // ════════════════════════════════════════

    private fun drawPinkBunny(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        // Ears
        val earP = fill(0xFFFFB6C1.toInt())
        val earInner = fill(0xFFFF69B4.toInt())
        canvas.drawOval(RectF(cx - r * 0.5f, cy - r * 1.8f, cx - r * 0.1f, cy - r * 0.3f), earP)
        canvas.drawOval(RectF(cx + r * 0.1f, cy - r * 1.8f, cx + r * 0.5f, cy - r * 0.3f), earP)
        canvas.drawOval(RectF(cx - r * 0.4f, cy - r * 1.5f, cx - r * 0.2f, cy - r * 0.5f), earInner)
        canvas.drawOval(RectF(cx + r * 0.2f, cy - r * 1.5f, cx + r * 0.4f, cy - r * 0.5f), earInner)
        // Head
        canvas.drawCircle(cx, cy, r, fill(0xFFFFD1DC.toInt()))
        // Face
        drawFace(canvas, cx, cy, s)
        // Nose
        canvas.drawCircle(cx, cy + s * 0.03f, s * 0.02f, fill(0xFFFF69B4.toInt()))
        drawSmile(canvas, cx, cy, s)
        drawBlush(canvas, cx, cy, s, r)
    }

    private fun drawKittyCat(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        // Ears
        val earP = fill(0xFFFFF5EE.toInt())
        val path1 = Path().apply { moveTo(cx - r, cy - r * 0.5f); lineTo(cx - r * 0.7f, cy - r * 1.5f); lineTo(cx - r * 0.2f, cy - r * 0.5f); close() }
        val path2 = Path().apply { moveTo(cx + r * 0.2f, cy - r * 0.5f); lineTo(cx + r * 0.7f, cy - r * 1.5f); lineTo(cx + r, cy - r * 0.5f); close() }
        canvas.drawPath(path1, earP)
        canvas.drawPath(path2, earP)
        val innerEar = fill(0xFFFFB6C1.toInt())
        val ip1 = Path().apply { moveTo(cx - r * 0.85f, cy - r * 0.55f); lineTo(cx - r * 0.7f, cy - r * 1.2f); lineTo(cx - r * 0.35f, cy - r * 0.55f); close() }
        val ip2 = Path().apply { moveTo(cx + r * 0.35f, cy - r * 0.55f); lineTo(cx + r * 0.7f, cy - r * 1.2f); lineTo(cx + r * 0.85f, cy - r * 0.55f); close() }
        canvas.drawPath(ip1, innerEar)
        canvas.drawPath(ip2, innerEar)
        // Head
        canvas.drawCircle(cx, cy, r, fill(0xFFFFF5EE.toInt()))
        drawFace(canvas, cx, cy, s)
        // Nose triangle
        val nose = Path().apply { moveTo(cx, cy + s * 0.02f); lineTo(cx - s * 0.02f, cy + s * 0.04f); lineTo(cx + s * 0.02f, cy + s * 0.04f); close() }
        canvas.drawPath(nose, fill(0xFFFF69B4.toInt()))
        // Whiskers
        val wp = stroke(0xFF999999.toInt(), s * 0.008f)
        canvas.drawLine(cx - r * 0.9f, cy + s * 0.02f, cx - r * 0.4f, cy + s * 0.01f, wp)
        canvas.drawLine(cx - r * 0.9f, cy + s * 0.05f, cx - r * 0.4f, cy + s * 0.04f, wp)
        canvas.drawLine(cx + r * 0.4f, cy + s * 0.01f, cx + r * 0.9f, cy + s * 0.02f, wp)
        canvas.drawLine(cx + r * 0.4f, cy + s * 0.04f, cx + r * 0.9f, cy + s * 0.05f, wp)
        drawSmile(canvas, cx, cy, s)
        drawBlush(canvas, cx, cy, s, r)
    }

    private fun drawTeddyBear(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        val brown = fill(0xFFD2B48C.toInt())
        val darkBrown = fill(0xFFC4A882.toInt())
        // Ears
        canvas.drawCircle(cx - r * 0.8f, cy - r * 0.7f, r * 0.4f, brown)
        canvas.drawCircle(cx - r * 0.8f, cy - r * 0.7f, r * 0.2f, darkBrown)
        canvas.drawCircle(cx + r * 0.8f, cy - r * 0.7f, r * 0.4f, brown)
        canvas.drawCircle(cx + r * 0.8f, cy - r * 0.7f, r * 0.2f, darkBrown)
        // Head
        canvas.drawCircle(cx, cy, r, brown)
        // Muzzle
        canvas.drawCircle(cx, cy + r * 0.25f, r * 0.4f, darkBrown)
        canvas.drawCircle(cx, cy + r * 0.15f, s * 0.025f, fill(Color.BLACK))
        drawFace(canvas, cx, cy - s * 0.02f, s)
        drawSmile(canvas, cx, cy, s, s * 0.01f)
        drawBlush(canvas, cx, cy, s, r)
    }

    private fun drawBabyPanda(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        // Ears
        canvas.drawCircle(cx - r * 0.7f, cy - r * 0.7f, r * 0.35f, fill(Color.BLACK))
        canvas.drawCircle(cx + r * 0.7f, cy - r * 0.7f, r * 0.35f, fill(Color.BLACK))
        // Head
        canvas.drawCircle(cx, cy, r, fill(Color.WHITE))
        // Eye patches
        canvas.drawOval(RectF(cx - r * 0.5f, cy - r * 0.35f, cx - r * 0.1f, cy + r * 0.15f), fill(Color.BLACK))
        canvas.drawOval(RectF(cx + r * 0.1f, cy - r * 0.35f, cx + r * 0.5f, cy + r * 0.15f), fill(Color.BLACK))
        // Eyes in patches
        canvas.drawCircle(cx - r * 0.3f, cy - r * 0.1f, s * 0.03f, fill(Color.WHITE))
        canvas.drawCircle(cx + r * 0.3f, cy - r * 0.1f, s * 0.03f, fill(Color.WHITE))
        canvas.drawCircle(cx - r * 0.28f, cy - r * 0.08f, s * 0.015f, fill(Color.BLACK))
        canvas.drawCircle(cx + r * 0.32f, cy - r * 0.08f, s * 0.015f, fill(Color.BLACK))
        // Nose
        canvas.drawOval(RectF(cx - s * 0.025f, cy + r * 0.15f, cx + s * 0.025f, cy + r * 0.3f), fill(Color.BLACK))
        drawSmile(canvas, cx, cy + s * 0.03f, s)
        drawBlush(canvas, cx, cy + s * 0.01f, s, r)
    }

    private fun drawLittleDuck(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        val yellow = fill(0xFFFFD700.toInt())
        // Body
        canvas.drawCircle(cx, cy + r * 0.3f, r * 0.9f, yellow)
        // Head
        canvas.drawCircle(cx, cy - r * 0.3f, r * 0.65f, yellow)
        drawFace(canvas, cx, cy - r * 0.35f, s)
        // Beak
        val beak = Path().apply { moveTo(cx - s * 0.04f, cy - r * 0.2f); lineTo(cx + s * 0.04f, cy - r * 0.2f); lineTo(cx, cy - r * 0.05f); close() }
        canvas.drawPath(beak, fill(0xFFFF8C00.toInt()))
        drawBlush(canvas, cx, cy - r * 0.25f, s, r * 0.5f)
    }

    private fun drawShibaInu(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        val orange = fill(0xFFFFA500.toInt())
        val white = fill(Color.WHITE)
        // Ears
        val le = Path().apply { moveTo(cx - r, cy - r * 0.3f); lineTo(cx - r * 0.6f, cy - r * 1.4f); lineTo(cx - r * 0.15f, cy - r * 0.3f); close() }
        val re = Path().apply { moveTo(cx + r * 0.15f, cy - r * 0.3f); lineTo(cx + r * 0.6f, cy - r * 1.4f); lineTo(cx + r, cy - r * 0.3f); close() }
        canvas.drawPath(le, orange)
        canvas.drawPath(re, orange)
        // Head
        canvas.drawCircle(cx, cy, r, orange)
        // White face
        canvas.drawOval(RectF(cx - r * 0.65f, cy - r * 0.2f, cx + r * 0.65f, cy + r * 0.9f), white)
        drawFace(canvas, cx, cy - s * 0.02f, s)
        canvas.drawCircle(cx, cy + s * 0.02f, s * 0.02f, fill(Color.BLACK))
        drawSmile(canvas, cx, cy, s)
    }

    private fun drawBabyPenguin(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        val dark = fill(0xFF2F4F4F.toInt())
        val white = fill(Color.WHITE)
        // Body
        canvas.drawOval(RectF(cx - r, cy - r * 0.8f, cx + r, cy + r * 1.1f), dark)
        canvas.drawOval(RectF(cx - r * 0.65f, cy - r * 0.4f, cx + r * 0.65f, cy + r * 0.9f), white)
        drawFace(canvas, cx, cy - s * 0.04f, s)
        // Beak
        val beak = Path().apply { moveTo(cx - s * 0.03f, cy); lineTo(cx + s * 0.03f, cy); lineTo(cx, cy + s * 0.04f); close() }
        canvas.drawPath(beak, fill(0xFFFFA500.toInt()))
        // Feet
        canvas.drawOval(RectF(cx - r * 0.5f, cy + r * 0.9f, cx - r * 0.1f, cy + r * 1.15f), fill(0xFFFFA500.toInt()))
        canvas.drawOval(RectF(cx + r * 0.1f, cy + r * 0.9f, cx + r * 0.5f, cy + r * 1.15f), fill(0xFFFFA500.toInt()))
        drawBlush(canvas, cx, cy - s * 0.01f, s, r * 0.6f)
    }

    private fun drawUnicorn(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        canvas.drawCircle(cx, cy, r, fill(Color.WHITE))
        // Horn
        val horn = Path().apply { moveTo(cx, cy - r * 1.3f); lineTo(cx - r * 0.15f, cy - r * 0.5f); lineTo(cx + r * 0.15f, cy - r * 0.5f); close() }
        canvas.drawPath(horn, fill(0xFFFFD700.toInt()))
        // Mane
        canvas.drawCircle(cx - r * 0.6f, cy - r * 0.4f, r * 0.2f, fill(0xFFFF69B4.toInt()))
        canvas.drawCircle(cx - r * 0.7f, cy, r * 0.2f, fill(0xFF87CEEB.toInt()))
        canvas.drawCircle(cx - r * 0.6f, cy + r * 0.4f, r * 0.2f, fill(0xFFDDA0DD.toInt()))
        drawFace(canvas, cx, cy, s)
        drawSmile(canvas, cx, cy, s)
        drawBlush(canvas, cx, cy, s, r)
    }

    private fun drawHamster(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        val tan = fill(0xFFF4A460.toInt())
        val cream = fill(0xFFFFDAB9.toInt())
        // Ears
        canvas.drawCircle(cx - r * 0.7f, cy - r * 0.6f, r * 0.3f, tan)
        canvas.drawCircle(cx + r * 0.7f, cy - r * 0.6f, r * 0.3f, tan)
        // Head
        canvas.drawCircle(cx, cy, r, tan)
        canvas.drawCircle(cx, cy + r * 0.15f, r * 0.6f, cream)
        // Cheeks
        canvas.drawCircle(cx - r * 0.55f, cy + r * 0.15f, r * 0.35f, fill(0xFFFFB6C1.toInt()))
        canvas.drawCircle(cx + r * 0.55f, cy + r * 0.15f, r * 0.35f, fill(0xFFFFB6C1.toInt()))
        drawFace(canvas, cx, cy - s * 0.02f, s)
        canvas.drawCircle(cx, cy + s * 0.015f, s * 0.015f, fill(Color.BLACK))
        drawSmile(canvas, cx, cy, s)
    }

    private fun drawPinkPiggy(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        val pink = fill(0xFFFFC0CB.toInt())
        val hotPink = fill(0xFFFF69B4.toInt())
        // Ears
        val le = Path().apply { moveTo(cx - r * 0.6f, cy - r * 0.5f); lineTo(cx - r * 0.8f, cy - r * 1.2f); lineTo(cx - r * 0.2f, cy - r * 0.5f); close() }
        val re = Path().apply { moveTo(cx + r * 0.2f, cy - r * 0.5f); lineTo(cx + r * 0.8f, cy - r * 1.2f); lineTo(cx + r * 0.6f, cy - r * 0.5f); close() }
        canvas.drawPath(le, pink)
        canvas.drawPath(re, pink)
        // Head
        canvas.drawCircle(cx, cy, r, pink)
        // Snout
        canvas.drawOval(RectF(cx - r * 0.35f, cy, cx + r * 0.35f, cy + r * 0.4f), hotPink)
        canvas.drawCircle(cx - r * 0.12f, cy + r * 0.2f, s * 0.015f, fill(0xFFFF1493.toInt()))
        canvas.drawCircle(cx + r * 0.12f, cy + r * 0.2f, s * 0.015f, fill(0xFFFF1493.toInt()))
        drawFace(canvas, cx, cy - s * 0.03f, s)
        drawBlush(canvas, cx, cy, s, r)
    }

    private fun drawBabySeal(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        val gray = fill(0xFFB0C4DE.toInt())
        canvas.drawOval(RectF(cx - r * 1.1f, cy - r * 0.7f, cx + r * 1.1f, cy + r * 1.0f), gray)
        canvas.drawCircle(cx, cy - r * 0.1f, r * 0.75f, fill(0xFFD6E4F0.toInt()))
        drawFace(canvas, cx, cy - s * 0.02f, s)
        canvas.drawCircle(cx, cy + s * 0.015f, s * 0.02f, fill(Color.BLACK))
        drawSmile(canvas, cx, cy, s)
        drawBlush(canvas, cx, cy, s, r * 0.6f)
    }

    private fun drawSleepingFox(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        val orange = fill(0xFFFF7F50.toInt())
        // Ears
        val le = Path().apply { moveTo(cx - r, cy - r * 0.3f); lineTo(cx - r * 0.6f, cy - r * 1.4f); lineTo(cx - r * 0.15f, cy - r * 0.3f); close() }
        val re = Path().apply { moveTo(cx + r * 0.15f, cy - r * 0.3f); lineTo(cx + r * 0.6f, cy - r * 1.4f); lineTo(cx + r, cy - r * 0.3f); close() }
        canvas.drawPath(le, orange)
        canvas.drawPath(re, orange)
        canvas.drawCircle(cx, cy, r, orange)
        // White muzzle
        canvas.drawOval(RectF(cx - r * 0.5f, cy, cx + r * 0.5f, cy + r * 0.7f), fill(Color.WHITE))
        // Closed eyes (sleeping)
        val ep = stroke(Color.BLACK, s * 0.015f)
        canvas.drawLine(cx - r * 0.3f, cy - r * 0.05f, cx - r * 0.1f, cy + r * 0.05f, ep)
        canvas.drawLine(cx + r * 0.1f, cy + r * 0.05f, cx + r * 0.3f, cy - r * 0.05f, ep)
        canvas.drawCircle(cx, cy + r * 0.2f, s * 0.015f, fill(Color.BLACK))
        drawBlush(canvas, cx, cy, s, r)
        // Zzz
        val zp = fill(0xFF87CEEB.toInt())
        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = 0xFF87CEEB.toInt(); textSize = s * 0.08f }
        canvas.drawText("z", cx + r * 0.6f, cy - r * 0.5f, textPaint)
        canvas.drawText("z", cx + r * 0.8f, cy - r * 0.8f, textPaint)
    }

    // ════════════════════════════════════════
    // CATEGORY 2: BATTERY CHARACTERS (13-24)
    // ════════════════════════════════════════

    private fun drawBatteryShell(canvas: Canvas, s: Float, bodyColor: Int, fillPercent: Float, fillColor: Int) {
        val margin = s * 0.08f
        val rect = RectF(margin, s * 0.25f, s - margin - s * 0.08f, s * 0.75f)
        val shellP = fill(bodyColor)
        shellP.style = Paint.Style.STROKE
        shellP.strokeWidth = s * 0.03f
        canvas.drawRoundRect(rect, s * 0.05f, s * 0.05f, shellP)
        // Tip
        canvas.drawRoundRect(RectF(s - margin - s * 0.08f, s * 0.37f, s - margin, s * 0.63f), s * 0.02f, s * 0.02f, fill(bodyColor))
        // Fill
        val fillWidth = (rect.width() - s * 0.04f) * fillPercent
        val fillRect = RectF(rect.left + s * 0.02f, rect.top + s * 0.02f, rect.left + s * 0.02f + fillWidth, rect.bottom - s * 0.02f)
        canvas.drawRoundRect(fillRect, s * 0.03f, s * 0.03f, fill(fillColor))
    }

    private fun drawHeartBattery(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        drawBatteryShell(canvas, s, 0xFFFF6B8B.toInt(), 0.7f, 0xFFFFB6C1.toInt())
        drawHeart(canvas, cx - s * 0.05f, cy, s * 0.18f, 0xFFFF6B8B.toInt())
    }

    private fun drawBunnyBattery(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        drawBatteryShell(canvas, s, 0xFFFFB6C1.toInt(), 0.8f, 0xFFFFD1DC.toInt())
        // Mini bunny face
        val bx = cx + s * 0.12f
        canvas.drawCircle(bx, cy, s * 0.08f, fill(0xFFFFD1DC.toInt()))
        canvas.drawOval(RectF(bx - s * 0.03f, cy - s * 0.15f, bx - s * 0.01f, cy - s * 0.03f), fill(0xFFFFB6C1.toInt()))
        canvas.drawOval(RectF(bx + s * 0.01f, cy - s * 0.15f, bx + s * 0.03f, cy - s * 0.03f), fill(0xFFFFB6C1.toInt()))
        canvas.drawCircle(bx - s * 0.02f, cy - s * 0.01f, s * 0.008f, fill(Color.BLACK))
        canvas.drawCircle(bx + s * 0.02f, cy - s * 0.01f, s * 0.008f, fill(Color.BLACK))
    }

    private fun drawStarBattery(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        drawBatteryShell(canvas, s, 0xFFFFD700.toInt(), 0.6f, 0xFFFFF8DC.toInt())
        drawStar(canvas, cx + s * 0.1f, cy, s * 0.1f, 0xFFFFD700.toInt())
    }

    private fun drawCatBattery(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        drawBatteryShell(canvas, s, 0xFFFFF5EE.toInt(), 0.4f, 0xFFFF6347.toInt())
        // Cat face
        val bx = cx + s * 0.12f
        canvas.drawCircle(bx, cy, s * 0.07f, fill(0xFFFFF5EE.toInt()))
        val le = Path().apply { moveTo(bx - s * 0.05f, cy - s * 0.02f); lineTo(bx - s * 0.03f, cy - s * 0.09f); lineTo(bx - s * 0.01f, cy - s * 0.02f); close() }
        val re = Path().apply { moveTo(bx + s * 0.01f, cy - s * 0.02f); lineTo(bx + s * 0.03f, cy - s * 0.09f); lineTo(bx + s * 0.05f, cy - s * 0.02f); close() }
        canvas.drawPath(le, fill(0xFFFFF5EE.toInt()))
        canvas.drawPath(re, fill(0xFFFFF5EE.toInt()))
    }

    private fun drawPeachBattery(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        drawBatteryShell(canvas, s, 0xFFFFDAB9.toInt(), 0.75f, 0xFFFFB6C1.toInt())
        canvas.drawCircle(cx + s * 0.12f, cy, s * 0.06f, fill(0xFFFFDAB9.toInt()))
    }

    private fun drawCloudBattery(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        drawBatteryShell(canvas, s, 0xFF87CEEB.toInt(), 0.5f, 0xFFE0FFFF.toInt())
        val cloudX = cx + s * 0.1f
        canvas.drawCircle(cloudX, cy, s * 0.04f, fill(Color.WHITE))
        canvas.drawCircle(cloudX - s * 0.03f, cy + s * 0.015f, s * 0.03f, fill(Color.WHITE))
        canvas.drawCircle(cloudX + s * 0.03f, cy + s * 0.015f, s * 0.03f, fill(Color.WHITE))
    }

    private fun drawBobaBattery(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        drawBatteryShell(canvas, s, 0xFFF5DEB3.toInt(), 0.65f, 0xFFD2B48C.toInt())
        // Boba dots
        canvas.drawCircle(cx + s * 0.08f, cy + s * 0.03f, s * 0.015f, fill(0xFF333333.toInt()))
        canvas.drawCircle(cx + s * 0.12f, cy + s * 0.02f, s * 0.015f, fill(0xFF333333.toInt()))
        canvas.drawCircle(cx + s * 0.1f, cy - s * 0.02f, s * 0.015f, fill(0xFF333333.toInt()))
    }

    private fun drawStrawberryBattery(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        drawBatteryShell(canvas, s, 0xFFFF6B8B.toInt(), 0.85f, 0xFFFF1493.toInt())
        // Mini strawberry
        canvas.drawCircle(cx + s * 0.12f, cy + s * 0.01f, s * 0.05f, fill(0xFFFF2400.toInt()))
        canvas.drawOval(RectF(cx + s * 0.08f, cy - s * 0.05f, cx + s * 0.16f, cy - s * 0.02f), fill(0xFF228B22.toInt()))
    }

    private fun drawFlowerBattery(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        drawBatteryShell(canvas, s, 0xFFFFB6C1.toInt(), 0.7f, 0xFFFF69B4.toInt())
        val fx = cx + s * 0.12f
        val pr = s * 0.025f
        for (i in 0 until 5) {
            val angle = Math.toRadians((i * 72).toDouble())
            canvas.drawCircle(fx + (s * 0.04f * cos(angle)).toFloat(), cy + (s * 0.04f * sin(angle)).toFloat(), pr, fill(0xFFFFB6C1.toInt()))
        }
        canvas.drawCircle(fx, cy, pr * 0.8f, fill(0xFFFFD700.toInt()))
    }

    private fun drawMoonBattery(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        drawBatteryShell(canvas, s, 0xFF191970.toInt(), 0.55f, 0xFF4169E1.toInt())
        // Crescent
        canvas.drawCircle(cx + s * 0.12f, cy, s * 0.06f, fill(0xFFFFD700.toInt()))
        canvas.drawCircle(cx + s * 0.14f, cy - s * 0.015f, s * 0.045f, fill(0xFF191970.toInt()))
    }

    private fun drawRainbowBattery(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        drawBatteryShell(canvas, s, 0xFFFF69B4.toInt(), 0.9f, 0xFFFFB6C1.toInt())
        val arcP = stroke(0xFFFF6347.toInt(), s * 0.015f)
        canvas.drawArc(RectF(cx + s * 0.04f, cy - s * 0.06f, cx + s * 0.2f, cy + s * 0.06f), 180f, 180f, false, arcP)
        arcP.color = 0xFFFFD700.toInt()
        canvas.drawArc(RectF(cx + s * 0.05f, cy - s * 0.05f, cx + s * 0.19f, cy + s * 0.05f), 180f, 180f, false, arcP)
        arcP.color = 0xFF87CEEB.toInt()
        canvas.drawArc(RectF(cx + s * 0.06f, cy - s * 0.04f, cx + s * 0.18f, cy + s * 0.04f), 180f, 180f, false, arcP)
    }

    private fun drawDiamondBattery(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        drawBatteryShell(canvas, s, 0xFFE0EEEE.toInt(), 1.0f, 0xFF87CEEB.toInt())
        val dx = cx + s * 0.12f
        val dp = Path().apply { moveTo(dx, cy - s * 0.06f); lineTo(dx + s * 0.05f, cy); lineTo(dx, cy + s * 0.06f); lineTo(dx - s * 0.05f, cy); close() }
        canvas.drawPath(dp, fill(0xFFE0EEEE.toInt()))
    }

    // ════════════════════════════════════════
    // CATEGORY 3: FASHION & BEAUTY (25-36)
    // ════════════════════════════════════════

    private fun drawPinkBow(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        val bowP = fill(0xFFFF6B8B.toInt())
        val lp = Path().apply { moveTo(cx, cy); cubicTo(cx - r, cy - r * 0.5f, cx - r * 1.2f, cy + r * 0.3f, cx, cy); close() }
        val rp = Path().apply { moveTo(cx, cy); cubicTo(cx + r, cy - r * 0.5f, cx + r * 1.2f, cy + r * 0.3f, cx, cy); close() }
        canvas.drawPath(lp, bowP)
        canvas.drawPath(rp, bowP)
        canvas.drawCircle(cx, cy, r * 0.2f, fill(0xFFFFB6C1.toInt()))
        // Tails
        canvas.drawLine(cx - s * 0.02f, cy + r * 0.2f, cx - s * 0.06f, cy + r * 0.8f, stroke(0xFFFF6B8B.toInt(), s * 0.02f))
        canvas.drawLine(cx + s * 0.02f, cy + r * 0.2f, cx + s * 0.06f, cy + r * 0.8f, stroke(0xFFFF6B8B.toInt(), s * 0.02f))
    }

    private fun drawLipstick(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        // Tube
        canvas.drawRoundRect(RectF(cx - r * 0.3f, cy, cx + r * 0.3f, cy + r * 0.9f), s * 0.02f, s * 0.02f, fill(0xFF333333.toInt()))
        canvas.drawRoundRect(RectF(cx - r * 0.25f, cy + r * 0.1f, cx + r * 0.25f, cy + r * 0.85f), s * 0.01f, s * 0.01f, fill(0xFFFFD700.toInt()))
        // Lipstick tip
        val tip = Path().apply { moveTo(cx - r * 0.3f, cy); lineTo(cx + r * 0.3f, cy); lineTo(cx + r * 0.3f, cy - r * 0.6f); lineTo(cx - r * 0.3f, cy - r * 0.3f); close() }
        canvas.drawPath(tip, fill(0xFFFF1493.toInt()))
    }

    private fun drawHighHeel(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        val shoe = Path().apply {
            moveTo(cx - r, cy + r * 0.3f)
            lineTo(cx - r * 0.3f, cy - r * 0.5f)
            lineTo(cx + r, cy - r * 0.5f)
            lineTo(cx + r, cy + r * 0.3f)
            close()
        }
        canvas.drawPath(shoe, fill(0xFFFF1493.toInt()))
        // Heel
        canvas.drawLine(cx - r * 0.7f, cy + r * 0.3f, cx - r * 0.5f, cy + r * 0.9f, stroke(0xFFFF1493.toInt(), s * 0.03f))
        // Sole
        canvas.drawLine(cx - r, cy + r * 0.3f, cx + r, cy + r * 0.3f, stroke(0xFFFF1493.toInt(), s * 0.025f))
    }

    private fun drawPrincessCrown(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        val gold = fill(0xFFFFD700.toInt())
        val crown = Path().apply {
            moveTo(cx - r, cy + r * 0.3f)
            lineTo(cx - r, cy - r * 0.2f)
            lineTo(cx - r * 0.5f, cy + r * 0.1f)
            lineTo(cx, cy - r * 0.6f)
            lineTo(cx + r * 0.5f, cy + r * 0.1f)
            lineTo(cx + r, cy - r * 0.2f)
            lineTo(cx + r, cy + r * 0.3f)
            close()
        }
        canvas.drawPath(crown, gold)
        // Gems
        canvas.drawCircle(cx, cy - r * 0.35f, s * 0.025f, fill(0xFFFF69B4.toInt()))
        canvas.drawCircle(cx - r * 0.5f, cy, s * 0.02f, fill(0xFF87CEEB.toInt()))
        canvas.drawCircle(cx + r * 0.5f, cy, s * 0.02f, fill(0xFF87CEEB.toInt()))
    }

    private fun drawHandbag(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        // Handle
        canvas.drawArc(RectF(cx - r * 0.4f, cy - r * 0.8f, cx + r * 0.4f, cy - r * 0.1f), 180f, 180f, false, stroke(0xFFFFB6C1.toInt(), s * 0.025f))
        // Body
        canvas.drawRoundRect(RectF(cx - r * 0.7f, cy - r * 0.1f, cx + r * 0.7f, cy + r * 0.8f), s * 0.04f, s * 0.04f, fill(0xFFFFB6C1.toInt()))
        // Clasp
        canvas.drawCircle(cx, cy - r * 0.1f, s * 0.025f, fill(0xFFFFD700.toInt()))
    }

    private fun drawPerfume(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        // Cap
        canvas.drawRoundRect(RectF(cx - r * 0.15f, cy - r * 0.9f, cx + r * 0.15f, cy - r * 0.4f), s * 0.02f, s * 0.02f, fill(0xFFFFD700.toInt()))
        // Spray nozzle
        canvas.drawRect(RectF(cx - r * 0.05f, cy - r * 0.4f, cx + r * 0.05f, cy - r * 0.25f), fill(0xFFC0C0C0.toInt()))
        // Bottle
        canvas.drawRoundRect(RectF(cx - r * 0.5f, cy - r * 0.25f, cx + r * 0.5f, cy + r * 0.8f), s * 0.04f, s * 0.04f, fill(0xFFE6E6FA.toInt()))
        // Label
        canvas.drawRoundRect(RectF(cx - r * 0.3f, cy + r * 0.05f, cx + r * 0.3f, cy + r * 0.4f), s * 0.02f, s * 0.02f, fill(0xFFDDA0DD.toInt()))
    }

    private fun drawNailPolish(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        // Cap
        canvas.drawRoundRect(RectF(cx - r * 0.2f, cy - r * 0.9f, cx + r * 0.2f, cy - r * 0.2f), s * 0.02f, s * 0.02f, fill(0xFF333333.toInt()))
        // Brush handle
        canvas.drawRect(RectF(cx - r * 0.05f, cy - r * 0.2f, cx + r * 0.05f, cy + r * 0.1f), fill(0xFFC0C0C0.toInt()))
        // Bottle
        canvas.drawRoundRect(RectF(cx - r * 0.4f, cy + r * 0.1f, cx + r * 0.4f, cy + r * 0.9f), s * 0.04f, s * 0.04f, fill(0xFFFF69B4.toInt()))
    }

    private fun drawDiamondRing(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        // Band
        canvas.drawArc(RectF(cx - r * 0.5f, cy, cx + r * 0.5f, cy + r * 0.8f), 0f, 180f, false, stroke(0xFFFFD700.toInt(), s * 0.03f))
        // Diamond
        val dp = Path().apply { moveTo(cx, cy - r * 0.3f); lineTo(cx + r * 0.35f, cy + r * 0.1f); lineTo(cx, cy + r * 0.4f); lineTo(cx - r * 0.35f, cy + r * 0.1f); close() }
        canvas.drawPath(dp, fill(0xFFE0EEEE.toInt()))
        canvas.drawPath(dp, stroke(0xFF87CEEB.toInt(), s * 0.01f))
        // Sparkle
        drawStar(canvas, cx + r * 0.3f, cy - r * 0.2f, s * 0.04f, 0xFFFFD700.toInt())
    }

    private fun drawSunglasses(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        val dark = fill(0xFF333333.toInt())
        // Bridge
        canvas.drawLine(cx - r * 0.1f, cy, cx + r * 0.1f, cy, stroke(0xFF333333.toInt(), s * 0.02f))
        // Left lens
        canvas.drawRoundRect(RectF(cx - r * 1.0f, cy - r * 0.35f, cx - r * 0.1f, cy + r * 0.35f), s * 0.04f, s * 0.04f, dark)
        // Right lens
        canvas.drawRoundRect(RectF(cx + r * 0.1f, cy - r * 0.35f, cx + r * 1.0f, cy + r * 0.35f), s * 0.04f, s * 0.04f, dark)
        // Shine
        canvas.drawLine(cx - r * 0.7f, cy - r * 0.15f, cx - r * 0.4f, cy - r * 0.15f, stroke(Color.WHITE, s * 0.015f))
        canvas.drawLine(cx + r * 0.4f, cy - r * 0.15f, cx + r * 0.7f, cy - r * 0.15f, stroke(Color.WHITE, s * 0.015f))
        // Arms
        canvas.drawLine(cx - r * 1.0f, cy, cx - r * 1.2f, cy - r * 0.2f, stroke(0xFF333333.toInt(), s * 0.015f))
        canvas.drawLine(cx + r * 1.0f, cy, cx + r * 1.2f, cy - r * 0.2f, stroke(0xFF333333.toInt(), s * 0.015f))
    }

    private fun drawPinkDress(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        val dress = Path().apply {
            moveTo(cx - r * 0.3f, cy - r * 0.8f)
            lineTo(cx + r * 0.3f, cy - r * 0.8f)
            lineTo(cx + r * 0.8f, cy + r * 0.9f)
            lineTo(cx - r * 0.8f, cy + r * 0.9f)
            close()
        }
        canvas.drawPath(dress, fill(0xFFFF69B4.toInt()))
        // Belt
        canvas.drawRect(RectF(cx - r * 0.35f, cy - r * 0.2f, cx + r * 0.35f, cy - r * 0.1f), fill(0xFFFFB6C1.toInt()))
        // Bow
        canvas.drawCircle(cx, cy - r * 0.15f, s * 0.02f, fill(0xFFFF1493.toInt()))
    }

    private fun drawPearlNecklace(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        val pearlP = fill(0xFFF8F8FF.toInt())
        val highlightP = fill(0xFFFFFFFF.toInt())
        val count = 10
        for (i in 0 until count) {
            val angle = Math.toRadians((180.0 + (i * 180.0 / (count - 1))))
            val px = cx + (r * 0.8f * cos(angle)).toFloat()
            val py = cy + (r * 0.6f * sin(angle)).toFloat()
            canvas.drawCircle(px, py, s * 0.03f, pearlP)
            canvas.drawCircle(px - s * 0.008f, py - s * 0.008f, s * 0.01f, highlightP)
        }
    }

    private fun drawHairRibbon(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        val ribbon = Path().apply {
            moveTo(cx, cy)
            quadTo(cx - r * 0.8f, cy - r * 0.6f, cx - r * 1.0f, cy + r * 0.3f)
            quadTo(cx - r * 0.4f, cy + r * 0.1f, cx, cy)
            quadTo(cx + r * 0.4f, cy + r * 0.1f, cx + r * 1.0f, cy + r * 0.3f)
            quadTo(cx + r * 0.8f, cy - r * 0.6f, cx, cy)
        }
        canvas.drawPath(ribbon, fill(0xFFFFC0CB.toInt()))
        canvas.drawCircle(cx, cy, r * 0.15f, fill(0xFFFF69B4.toInt()))
        // Tail
        canvas.drawLine(cx, cy + r * 0.15f, cx - r * 0.3f, cy + r * 0.8f, stroke(0xFFFFC0CB.toInt(), s * 0.025f))
        canvas.drawLine(cx, cy + r * 0.15f, cx + r * 0.3f, cy + r * 0.8f, stroke(0xFFFFC0CB.toInt(), s * 0.025f))
    }

    // ════════════════════════════════════════
    // CATEGORY 4: FOOD & DRINKS (37-48)
    // ════════════════════════════════════════

    private fun drawBobaTea(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        // Cup
        val cup = Path().apply { moveTo(cx - r * 0.6f, cy - r * 0.5f); lineTo(cx + r * 0.6f, cy - r * 0.5f); lineTo(cx + r * 0.45f, cy + r * 0.9f); lineTo(cx - r * 0.45f, cy + r * 0.9f); close() }
        canvas.drawPath(cup, fill(0xFFFFF0F5.toInt()))
        canvas.drawPath(cup, stroke(0xFFFFB6C1.toInt(), s * 0.015f))
        // Tea color
        val tea = Path().apply { moveTo(cx - r * 0.55f, cy - r * 0.2f); lineTo(cx + r * 0.55f, cy - r * 0.2f); lineTo(cx + r * 0.45f, cy + r * 0.9f); lineTo(cx - r * 0.45f, cy + r * 0.9f); close() }
        canvas.drawPath(tea, fill(0xFFF5DEB3.toInt()))
        // Boba balls
        val bobaP = fill(0xFF333333.toInt())
        canvas.drawCircle(cx - r * 0.2f, cy + r * 0.6f, s * 0.025f, bobaP)
        canvas.drawCircle(cx + r * 0.15f, cy + r * 0.55f, s * 0.025f, bobaP)
        canvas.drawCircle(cx, cy + r * 0.7f, s * 0.025f, bobaP)
        canvas.drawCircle(cx - r * 0.1f, cy + r * 0.45f, s * 0.025f, bobaP)
        canvas.drawCircle(cx + r * 0.25f, cy + r * 0.7f, s * 0.025f, bobaP)
        // Straw
        canvas.drawLine(cx + r * 0.1f, cy - r * 1.0f, cx + r * 0.05f, cy + r * 0.3f, stroke(0xFFFF69B4.toInt(), s * 0.025f))
        // Lid
        canvas.drawRoundRect(RectF(cx - r * 0.65f, cy - r * 0.55f, cx + r * 0.65f, cy - r * 0.45f), s * 0.02f, s * 0.02f, fill(0xFFFFB6C1.toInt()))
    }

    private fun drawCupcake(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        // Wrapper
        val wrapper = Path().apply { moveTo(cx - r * 0.6f, cy + r * 0.1f); lineTo(cx + r * 0.6f, cy + r * 0.1f); lineTo(cx + r * 0.45f, cy + r * 0.9f); lineTo(cx - r * 0.45f, cy + r * 0.9f); close() }
        canvas.drawPath(wrapper, fill(0xFFFFB6C1.toInt()))
        // Frosting
        canvas.drawCircle(cx, cy - r * 0.1f, r * 0.55f, fill(0xFFFF69B4.toInt()))
        canvas.drawCircle(cx - r * 0.3f, cy + r * 0.05f, r * 0.3f, fill(0xFFFF69B4.toInt()))
        canvas.drawCircle(cx + r * 0.3f, cy + r * 0.05f, r * 0.3f, fill(0xFFFF69B4.toInt()))
        // Cherry
        canvas.drawCircle(cx, cy - r * 0.5f, s * 0.03f, fill(0xFFFF2400.toInt()))
        canvas.drawCircle(cx - s * 0.005f, cy - r * 0.52f, s * 0.008f, fill(Color.WHITE))
    }

    private fun drawDonut(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        canvas.drawCircle(cx, cy, r, fill(0xFFFFD1DC.toInt()))
        canvas.drawCircle(cx, cy, r * 0.35f, fill(0xFFFFF0F5.toInt()))
        // Icing drip
        val icing = Path().apply {
            moveTo(cx - r, cy - r * 0.1f)
            quadTo(cx - r * 0.5f, cy - r * 0.5f, cx, cy - r * 0.3f)
            quadTo(cx + r * 0.5f, cy - r * 0.6f, cx + r, cy - r * 0.1f)
        }
        canvas.drawPath(icing, stroke(0xFFFF69B4.toInt(), s * 0.03f))
        // Sprinkles
        val colors = intArrayOf(0xFFFF6347.toInt(), 0xFF87CEEB.toInt(), 0xFFFFD700.toInt(), 0xFF9ACD32.toInt())
        for (i in 0 until 8) {
            val angle = Math.toRadians((i * 45).toDouble())
            val sr = r * 0.7f
            val sx = cx + (sr * cos(angle)).toFloat()
            val sy = cy + (sr * sin(angle)).toFloat()
            canvas.drawCircle(sx, sy, s * 0.012f, fill(colors[i % colors.size]))
        }
    }

    private fun drawIceCream(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        // Cone
        val cone = Path().apply { moveTo(cx - r * 0.5f, cy + r * 0.1f); lineTo(cx + r * 0.5f, cy + r * 0.1f); lineTo(cx, cy + r * 1.1f); close() }
        canvas.drawPath(cone, fill(0xFFDEB887.toInt()))
        // Cross-hatch
        canvas.drawLine(cx - r * 0.3f, cy + r * 0.35f, cx + r * 0.1f, cy + r * 0.8f, stroke(0xFFC4A882.toInt(), s * 0.008f))
        canvas.drawLine(cx + r * 0.3f, cy + r * 0.35f, cx - r * 0.1f, cy + r * 0.8f, stroke(0xFFC4A882.toInt(), s * 0.008f))
        // Scoops
        canvas.drawCircle(cx, cy - r * 0.1f, r * 0.45f, fill(0xFFFFC0CB.toInt()))
        canvas.drawCircle(cx - r * 0.25f, cy + r * 0.05f, r * 0.35f, fill(0xFFFFDAB9.toInt()))
        canvas.drawCircle(cx + r * 0.25f, cy + r * 0.05f, r * 0.35f, fill(0xFF9ACD32.toInt()))
    }

    private fun drawStrawberry(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        // Body
        canvas.drawCircle(cx, cy + r * 0.1f, r * 0.85f, fill(0xFFFF2400.toInt()))
        // Leaves
        val leaves = Path().apply {
            moveTo(cx - r * 0.4f, cy - r * 0.5f)
            quadTo(cx, cy - r * 1.0f, cx + r * 0.4f, cy - r * 0.5f)
            quadTo(cx, cy - r * 0.3f, cx - r * 0.4f, cy - r * 0.5f)
        }
        canvas.drawPath(leaves, fill(0xFF228B22.toInt()))
        // Seeds
        val seedP = fill(0xFFFFD700.toInt())
        canvas.drawCircle(cx - r * 0.25f, cy, s * 0.01f, seedP)
        canvas.drawCircle(cx + r * 0.25f, cy, s * 0.01f, seedP)
        canvas.drawCircle(cx, cy + r * 0.3f, s * 0.01f, seedP)
        canvas.drawCircle(cx - r * 0.15f, cy + r * 0.5f, s * 0.01f, seedP)
        canvas.drawCircle(cx + r * 0.15f, cy + r * 0.5f, s * 0.01f, seedP)
    }

    private fun drawCherry(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        // Stems
        val stemP = stroke(0xFF228B22.toInt(), s * 0.015f)
        canvas.drawLine(cx, cy - r * 0.8f, cx - r * 0.4f, cy + r * 0.1f, stemP)
        canvas.drawLine(cx, cy - r * 0.8f, cx + r * 0.4f, cy + r * 0.1f, stemP)
        // Leaf
        val leaf = Path().apply { moveTo(cx, cy - r * 0.8f); quadTo(cx + r * 0.5f, cy - r * 1.0f, cx + r * 0.3f, cy - r * 0.6f); quadTo(cx + r * 0.1f, cy - r * 0.7f, cx, cy - r * 0.8f) }
        canvas.drawPath(leaf, fill(0xFF228B22.toInt()))
        // Cherries
        canvas.drawCircle(cx - r * 0.4f, cy + r * 0.3f, r * 0.4f, fill(0xFFDC143C.toInt()))
        canvas.drawCircle(cx + r * 0.4f, cy + r * 0.3f, r * 0.4f, fill(0xFFDC143C.toInt()))
        // Shine
        canvas.drawCircle(cx - r * 0.5f, cy + r * 0.15f, s * 0.02f, fill(Color.WHITE))
        canvas.drawCircle(cx + r * 0.3f, cy + r * 0.15f, s * 0.02f, fill(Color.WHITE))
    }

    private fun drawLollipop(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        // Stick
        canvas.drawLine(cx, cy + r * 0.3f, cx, cy + r * 1.1f, stroke(Color.WHITE, s * 0.03f))
        // Candy circle
        canvas.drawCircle(cx, cy - r * 0.1f, r * 0.7f, fill(0xFFFF69B4.toInt()))
        // Spiral
        val spiralP = stroke(0xFFFF1493.toInt(), s * 0.02f)
        canvas.drawArc(RectF(cx - r * 0.5f, cy - r * 0.6f, cx + r * 0.5f, cy + r * 0.4f), 0f, 180f, false, spiralP)
        canvas.drawArc(RectF(cx - r * 0.3f, cy - r * 0.4f, cx + r * 0.3f, cy + r * 0.2f), 180f, 180f, false, spiralP)
    }

    private fun drawMacaron(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        val top = fill(0xFFE6E6FA.toInt())
        val bottom = fill(0xFFDDA0DD.toInt())
        val cream = fill(0xFFFFF8DC.toInt())
        // Bottom shell
        canvas.drawOval(RectF(cx - r * 0.7f, cy + r * 0.05f, cx + r * 0.7f, cy + r * 0.5f), bottom)
        // Cream filling
        canvas.drawRect(RectF(cx - r * 0.6f, cy - r * 0.05f, cx + r * 0.6f, cy + r * 0.1f), cream)
        // Top shell
        canvas.drawOval(RectF(cx - r * 0.7f, cy - r * 0.5f, cx + r * 0.7f, cy + r * 0.05f), top)
        // Ruffled edges
        val ruffleP = stroke(0xFFD8BFD8.toInt(), s * 0.008f)
        canvas.drawLine(cx - r * 0.65f, cy - r * 0.02f, cx + r * 0.65f, cy - r * 0.02f, ruffleP)
    }

    private fun drawCookie(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        canvas.drawCircle(cx, cy, r * 0.9f, fill(0xFFD2691E.toInt()))
        // Chips
        val chipP = fill(0xFF8B4513.toInt())
        canvas.drawCircle(cx - r * 0.3f, cy - r * 0.2f, s * 0.025f, chipP)
        canvas.drawCircle(cx + r * 0.2f, cy - r * 0.35f, s * 0.02f, chipP)
        canvas.drawCircle(cx + r * 0.35f, cy + r * 0.15f, s * 0.025f, chipP)
        canvas.drawCircle(cx - r * 0.15f, cy + r * 0.35f, s * 0.02f, chipP)
        canvas.drawCircle(cx + r * 0.05f, cy + r * 0.05f, s * 0.022f, chipP)
        // Bite
        canvas.drawCircle(cx + r * 0.7f, cy - r * 0.5f, r * 0.3f, fill(0x00000000))
    }

    private fun drawWatermelon(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        // Rind
        canvas.drawArc(RectF(cx - r, cy - r * 0.3f, cx + r, cy + r * 1.3f), 180f, 180f, true, fill(0xFF228B22.toInt()))
        // Flesh
        canvas.drawArc(RectF(cx - r * 0.85f, cy - r * 0.15f, cx + r * 0.85f, cy + r * 1.15f), 180f, 180f, true, fill(0xFFFF6347.toInt()))
        // Seeds
        val seedP = fill(0xFF333333.toInt())
        canvas.drawOval(RectF(cx - r * 0.15f, cy + r * 0.1f, cx - r * 0.08f, cy + r * 0.25f), seedP)
        canvas.drawOval(RectF(cx + r * 0.08f, cy + r * 0.15f, cx + r * 0.15f, cy + r * 0.3f), seedP)
        canvas.drawOval(RectF(cx - r * 0.35f, cy + r * 0.25f, cx - r * 0.28f, cy + r * 0.4f), seedP)
        canvas.drawOval(RectF(cx + r * 0.28f, cy + r * 0.2f, cx + r * 0.35f, cy + r * 0.35f), seedP)
    }

    private fun drawPancake(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        // Stack
        val pancakeP = fill(0xFFF4A460.toInt())
        canvas.drawOval(RectF(cx - r * 0.8f, cy + r * 0.2f, cx + r * 0.8f, cy + r * 0.6f), pancakeP)
        canvas.drawOval(RectF(cx - r * 0.8f, cy - r * 0.1f, cx + r * 0.8f, cy + r * 0.3f), pancakeP)
        canvas.drawOval(RectF(cx - r * 0.8f, cy - r * 0.4f, cx + r * 0.8f, cy + r * 0.0f), pancakeP)
        // Butter
        canvas.drawRoundRect(RectF(cx - r * 0.15f, cy - r * 0.6f, cx + r * 0.15f, cy - r * 0.4f), s * 0.02f, s * 0.02f, fill(0xFFFFD700.toInt()))
        // Syrup drip
        canvas.drawLine(cx + r * 0.5f, cy - r * 0.2f, cx + r * 0.6f, cy + r * 0.4f, stroke(0xFFCD853F.toInt(), s * 0.025f))
    }

    private fun drawCoffeeCup(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        // Cup
        canvas.drawRoundRect(RectF(cx - r * 0.6f, cy - r * 0.2f, cx + r * 0.6f, cy + r * 0.8f), s * 0.03f, s * 0.03f, fill(Color.WHITE))
        canvas.drawRoundRect(RectF(cx - r * 0.6f, cy - r * 0.2f, cx + r * 0.6f, cy + r * 0.8f), s * 0.03f, s * 0.03f, stroke(0xFFD2B48C.toInt(), s * 0.015f))
        // Coffee
        canvas.drawRoundRect(RectF(cx - r * 0.5f, cy + r * 0.0f, cx + r * 0.5f, cy + r * 0.7f), s * 0.02f, s * 0.02f, fill(0xFFD2B48C.toInt()))
        // Handle
        canvas.drawArc(RectF(cx + r * 0.55f, cy + r * 0.05f, cx + r * 0.9f, cy + r * 0.55f), -90f, 180f, false, stroke(Color.WHITE, s * 0.02f))
        // Steam
        val steamP = stroke(0x80C0C0C0.toInt(), s * 0.012f)
        canvas.drawLine(cx - r * 0.15f, cy - r * 0.3f, cx - r * 0.2f, cy - r * 0.7f, steamP)
        canvas.drawLine(cx, cy - r * 0.35f, cx + r * 0.05f, cy - r * 0.75f, steamP)
        canvas.drawLine(cx + r * 0.15f, cy - r * 0.3f, cx + r * 0.2f, cy - r * 0.7f, steamP)
    }

    // ════════════════════════════════════════
    // CATEGORY 5: MAGIC & HEARTS (49-60)
    // ════════════════════════════════════════

    private fun drawBeatingHeart(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        drawHeart(canvas, cx, cy, r * 0.9f, 0xFFFF6B8B.toInt())
        drawHeart(canvas, cx, cy - r * 0.05f, r * 0.55f, 0xFFFF1493.toInt())
        // Shine
        canvas.drawCircle(cx - r * 0.2f, cy - r * 0.15f, s * 0.025f, fill(0x60FFFFFF))
    }

    private fun drawSparkleStar(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        drawStar(canvas, cx, cy, r * 0.85f, 0xFFFFD700.toInt())
        drawStar(canvas, cx, cy, r * 0.5f, 0xFFFFF8DC.toInt())
    }

    private fun drawStar(canvas: Canvas, cx: Float, cy: Float, size: Float, color: Int) {
        val p = fill(color)
        val path = Path().apply {
            moveTo(cx, cy - size)
            lineTo(cx + size * 0.22f, cy - size * 0.3f)
            lineTo(cx + size * 0.95f, cy - size * 0.3f)
            lineTo(cx + size * 0.35f, cy + size * 0.1f)
            lineTo(cx + size * 0.6f, cy + size * 0.85f)
            lineTo(cx, cy + size * 0.4f)
            lineTo(cx - size * 0.6f, cy + size * 0.85f)
            lineTo(cx - size * 0.35f, cy + size * 0.1f)
            lineTo(cx - size * 0.95f, cy - size * 0.3f)
            lineTo(cx - size * 0.22f, cy - size * 0.3f)
            close()
        }
        canvas.drawPath(path, p)
    }

    private fun drawMagicWand(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        // Wand
        canvas.drawLine(cx - r * 0.5f, cy + r * 0.8f, cx + r * 0.3f, cy - r * 0.5f, stroke(0xFFD8BFD8.toInt(), s * 0.03f))
        // Star at tip
        drawStar(canvas, cx + r * 0.35f, cy - r * 0.6f, r * 0.35f, 0xFFFFD700.toInt())
        // Sparkles
        drawStar(canvas, cx + r * 0.7f, cy - r * 0.3f, r * 0.1f, 0xFFFFD700.toInt())
        drawStar(canvas, cx + r * 0.1f, cy - r * 0.8f, r * 0.08f, 0xFFFFD700.toInt())
    }

    private fun drawSakuraFlower(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        val petalP = fill(0xFFFFB6C1.toInt())
        for (i in 0 until 5) {
            val angle = Math.toRadians((i * 72 - 90).toDouble())
            val px = cx + (r * 0.55f * cos(angle)).toFloat()
            val py = cy + (r * 0.55f * sin(angle)).toFloat()
            canvas.drawCircle(px, py, r * 0.4f, petalP)
        }
        canvas.drawCircle(cx, cy, r * 0.25f, fill(0xFFFF69B4.toInt()))
        // Stamens
        for (i in 0 until 5) {
            val angle = Math.toRadians((i * 72 + 36 - 90).toDouble())
            val px = cx + (r * 0.15f * cos(angle)).toFloat()
            val py = cy + (r * 0.15f * sin(angle)).toFloat()
            canvas.drawCircle(px, py, s * 0.01f, fill(0xFFFFD700.toInt()))
        }
    }

    private fun drawButterfly(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        val wing1 = fill(0xFFFF69B4.toInt())
        val wing2 = fill(0xFFDDA0DD.toInt())
        // Left wings
        canvas.drawOval(RectF(cx - r * 1.1f, cy - r * 0.8f, cx - r * 0.1f, cy + r * 0.1f), wing1)
        canvas.drawOval(RectF(cx - r * 0.9f, cy + r * 0.0f, cx - r * 0.1f, cy + r * 0.7f), wing2)
        // Right wings
        canvas.drawOval(RectF(cx + r * 0.1f, cy - r * 0.8f, cx + r * 1.1f, cy + r * 0.1f), wing1)
        canvas.drawOval(RectF(cx + r * 0.1f, cy + r * 0.0f, cx + r * 0.9f, cy + r * 0.7f), wing2)
        // Body
        canvas.drawOval(RectF(cx - r * 0.08f, cy - r * 0.6f, cx + r * 0.08f, cy + r * 0.6f), fill(0xFF333333.toInt()))
        // Antennae
        canvas.drawLine(cx, cy - r * 0.6f, cx - r * 0.3f, cy - r * 1.0f, stroke(0xFF333333.toInt(), s * 0.01f))
        canvas.drawLine(cx, cy - r * 0.6f, cx + r * 0.3f, cy - r * 1.0f, stroke(0xFF333333.toInt(), s * 0.01f))
        canvas.drawCircle(cx - r * 0.3f, cy - r * 1.0f, s * 0.012f, fill(0xFF333333.toInt()))
        canvas.drawCircle(cx + r * 0.3f, cy - r * 1.0f, s * 0.012f, fill(0xFF333333.toInt()))
        // Wing dots
        canvas.drawCircle(cx - r * 0.5f, cy - r * 0.35f, s * 0.025f, fill(Color.WHITE))
        canvas.drawCircle(cx + r * 0.5f, cy - r * 0.35f, s * 0.025f, fill(Color.WHITE))
    }

    private fun drawRainbow(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        val colors = intArrayOf(0xFFFF0000.toInt(), 0xFFFF7F00.toInt(), 0xFFFFFF00.toInt(), 0xFF00FF00.toInt(), 0xFF0000FF.toInt(), 0xFF8B00FF.toInt())
        val bandWidth = s * 0.025f
        for (i in colors.indices) {
            val arcR = r - (i * bandWidth)
            val p = stroke(colors[i], bandWidth)
            canvas.drawArc(RectF(cx - arcR, cy - arcR * 0.3f, cx + arcR, cy + arcR * 1.3f), 180f, 180f, false, p)
        }
        // Clouds
        canvas.drawCircle(cx - r * 0.8f, cy + r * 0.15f, r * 0.25f, fill(Color.WHITE))
        canvas.drawCircle(cx + r * 0.8f, cy + r * 0.15f, r * 0.25f, fill(Color.WHITE))
    }

    private fun drawCrescentMoon(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        canvas.drawCircle(cx, cy, r * 0.85f, fill(0xFFFFD700.toInt()))
        canvas.drawCircle(cx + r * 0.3f, cy - r * 0.15f, r * 0.65f, fill(0xFF1A1A2E.toInt()))
        // Stars
        drawStar(canvas, cx - r * 0.6f, cy - r * 0.6f, r * 0.12f, 0xFFFFD700.toInt())
        drawStar(canvas, cx - r * 0.3f, cy + r * 0.7f, r * 0.08f, 0xFFFFD700.toInt())
        drawStar(canvas, cx + r * 0.7f, cy + r * 0.5f, r * 0.1f, 0xFFFFD700.toInt())
        // Face on moon
        canvas.drawCircle(cx - r * 0.15f, cy - r * 0.1f, s * 0.012f, fill(0xFF333333.toInt()))
        val smileP = stroke(0xFF333333.toInt(), s * 0.01f)
        canvas.drawArc(RectF(cx - r * 0.3f, cy, cx, cy + r * 0.25f), 0f, 180f, false, smileP)
    }

    private fun drawCrystalBall(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        // Base
        canvas.drawRoundRect(RectF(cx - r * 0.5f, cy + r * 0.7f, cx + r * 0.5f, cy + r * 0.95f), s * 0.02f, s * 0.02f, fill(0xFF8B7355.toInt()))
        canvas.drawRoundRect(RectF(cx - r * 0.35f, cy + r * 0.6f, cx + r * 0.35f, cy + r * 0.75f), s * 0.02f, s * 0.02f, fill(0xFFFFD700.toInt()))
        // Ball
        canvas.drawCircle(cx, cy - r * 0.05f, r * 0.7f, fill(0xFFE6E6FA.toInt()))
        // Shine
        canvas.drawArc(RectF(cx - r * 0.5f, cy - r * 0.55f, cx - r * 0.1f, cy - r * 0.15f), 200f, 120f, false, stroke(Color.WHITE, s * 0.015f))
        // Inner sparkle
        drawStar(canvas, cx - r * 0.15f, cy - r * 0.1f, r * 0.1f, 0xFFFFD700.toInt())
        drawStar(canvas, cx + r * 0.2f, cy + r * 0.15f, r * 0.08f, 0xFFFF69B4.toInt())
    }

    private fun drawMusicNote(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        // Note head
        canvas.drawOval(RectF(cx - r * 0.35f, cy + r * 0.2f, cx + r * 0.1f, cy + r * 0.6f), fill(0xFFFF1493.toInt()))
        // Stem
        canvas.drawLine(cx + r * 0.1f, cy + r * 0.35f, cx + r * 0.1f, cy - r * 0.7f, stroke(0xFFFF1493.toInt(), s * 0.025f))
        // Flag
        val flag = Path().apply {
            moveTo(cx + r * 0.1f, cy - r * 0.7f)
            quadTo(cx + r * 0.6f, cy - r * 0.4f, cx + r * 0.1f, cy - r * 0.2f)
        }
        canvas.drawPath(flag, stroke(0xFFFF1493.toInt(), s * 0.02f))
        // Sparkle
        drawStar(canvas, cx - r * 0.5f, cy - r * 0.4f, r * 0.12f, 0xFFFFD700.toInt())
    }

    private fun drawLoveLetter(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        // Envelope
        canvas.drawRoundRect(RectF(cx - r * 0.9f, cy - r * 0.4f, cx + r * 0.9f, cy + r * 0.7f), s * 0.02f, s * 0.02f, fill(0xFFFFF0F5.toInt()))
        canvas.drawRoundRect(RectF(cx - r * 0.9f, cy - r * 0.4f, cx + r * 0.9f, cy + r * 0.7f), s * 0.02f, s * 0.02f, stroke(0xFFFFB6C1.toInt(), s * 0.012f))
        // Flap
        val flap = Path().apply { moveTo(cx - r * 0.9f, cy - r * 0.4f); lineTo(cx, cy + r * 0.15f); lineTo(cx + r * 0.9f, cy - r * 0.4f) }
        canvas.drawPath(flap, fill(0xFFFFD1DC.toInt()))
        canvas.drawPath(flap, stroke(0xFFFFB6C1.toInt(), s * 0.012f))
        // Heart seal
        drawHeart(canvas, cx, cy - r * 0.15f, r * 0.25f, 0xFFFF6B8B.toInt())
    }

    private fun drawHeartBalloon(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        // String
        canvas.drawLine(cx, cy + r * 0.3f, cx, cy + r * 1.1f, stroke(0xFF999999.toInt(), s * 0.01f))
        canvas.drawLine(cx, cy + r * 1.1f, cx - r * 0.15f, cy + r * 1.2f, stroke(0xFF999999.toInt(), s * 0.01f))
        canvas.drawLine(cx, cy + r * 1.1f, cx + r * 0.15f, cy + r * 1.2f, stroke(0xFF999999.toInt(), s * 0.01f))
        // Heart balloon
        drawHeart(canvas, cx, cy - r * 0.1f, r * 0.75f, 0xFFFF6B8B.toInt())
        // Shine
        canvas.drawCircle(cx - r * 0.2f, cy - r * 0.25f, s * 0.025f, fill(0x50FFFFFF))
    }

    private fun drawLuckyClover(canvas: Canvas, s: Float, cx: Float, cy: Float, r: Float) {
        val green = fill(0xFF228B22.toInt())
        val lightGreen = fill(0xFF32CD32.toInt())
        // Four leaves
        canvas.drawCircle(cx - r * 0.3f, cy - r * 0.3f, r * 0.4f, green)
        canvas.drawCircle(cx + r * 0.3f, cy - r * 0.3f, r * 0.4f, green)
        canvas.drawCircle(cx - r * 0.3f, cy + r * 0.3f, r * 0.4f, green)
        canvas.drawCircle(cx + r * 0.3f, cy + r * 0.3f, r * 0.4f, green)
        // Center
        canvas.drawCircle(cx, cy, r * 0.15f, lightGreen)
        // Stem
        canvas.drawLine(cx, cy + r * 0.5f, cx + r * 0.15f, cy + r * 1.0f, stroke(0xFF228B22.toInt(), s * 0.025f))
        // Vein lines
        val veinP = stroke(0xFF006400.toInt(), s * 0.008f)
        canvas.drawLine(cx, cy, cx - r * 0.3f, cy - r * 0.3f, veinP)
        canvas.drawLine(cx, cy, cx + r * 0.3f, cy - r * 0.3f, veinP)
        canvas.drawLine(cx, cy, cx - r * 0.3f, cy + r * 0.3f, veinP)
        canvas.drawLine(cx, cy, cx + r * 0.3f, cy + r * 0.3f, veinP)
    }
}