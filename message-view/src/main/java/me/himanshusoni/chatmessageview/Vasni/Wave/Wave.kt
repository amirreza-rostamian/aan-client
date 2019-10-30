package me.himanshusoni.chatmessageview.Vasni.Wave

import android.graphics.Path
import me.himanshusoni.chatmessageview.Vasni.VasniSchema

/**
 * 水波对象
 * Created by SCWANG on 2017/12/11.
 */
internal class Wave /*extends View*/
//    int startColor;     //开始颜色
//    int closeColor;     //结束颜色
//    float alpha;        //颜色透明度

/**
 * 通过参数构造一个水波对象
 * @param offsetX   水平偏移量
 * @param offsetY   竖直偏移量
 * @param velocity  移动速度（像素/秒）
 * @param scaleX    水平拉伸量
 * @param scaleY    竖直拉伸量
 * @param w         波长
 * @param h         画布高度
 * @param wave      波幅（波宽度）
 */
//    @SuppressWarnings("PointlessArithmeticExpression")
(/*Context context, */offsetX: Int, offsetY: Int, velocity: Int, private val scaleX: Float       //水平拉伸比例
                      , private val scaleY: Float       //竖直拉伸比例
                      , widths: Int, height: Int, var wave: Int           //波幅（振幅）
) {

    var path: Path          //水波路径
    var width: Int = 0          //画布宽度（2倍波长）
    var offsetX: Float = 0.toFloat()        //水波的水平偏移量
    var offsetY: Float = 0.toFloat()        //水波的竖直偏移量
    var velocity: Float = 0.toFloat()       //水波移动速度（像素/秒）

    init {
        //        super(context);
        this.width = (2f * scaleX * widths.toFloat()).toInt() //画布宽度（2倍波长）
        this.offsetX = offsetX.toFloat()     //水平偏移量
        this.offsetY = offsetY.toFloat()     //竖直偏移量
        this.velocity = velocity.toFloat()   //移动速度（像素/秒）
        this.path = buildWavePath(width, height)
    }//波幅（波宽）
    //水平拉伸量
    //竖直拉伸量

    //    /*
    //     * 根据 波长度、中轴线高度、波幅 绘制水波路径
    //     */
    //    public Wave(Context context) {
    //        this(context, null, 0);
    //    }
    //
    //    public Wave(Context context, @Nullable AttributeSet attrs) {
    //        this(context, attrs, 0);
    //    }
    //
    //    public Wave(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    //        super(context, attrs, defStyleAttr);
    //
    //        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Wave);
    //
    ////        startColor = ta.getColor(R.styleable.Wave_mwhStartColor, 0);
    ////        closeColor = ta.getColor(R.styleable.Wave_mwhCloseColor, 0);
    ////        alpha = ta.getFloat(R.styleable.Wave_mwhColorAlpha, 0f);
    //        scaleX = ta.getFloat(R.styleable.Wave_mwScaleX, 1);
    //        scaleY = ta.getFloat(R.styleable.Wave_mwScaleY, 1);
    //        offsetX = ta.getDimensionPixelOffset(R.styleable.Wave_mwOffsetX, 0);
    //        offsetY = ta.getDimensionPixelOffset(R.styleable.Wave_mwOffsetY, 0);
    //        velocity = ta.getDimensionPixelOffset(R.styleable.Wave_mwVelocity, Util.dp2px(10));
    //        wave = ta.getDimensionPixelOffset(R.styleable.Wave_mwWaveHeight, 0) / 2;
    //
    //        ta.recycle();
    //    }

    fun updateWavePath(width: Int, height: Int, waveHeight: Int) {
        this.wave = if (wave > 0) wave else waveHeight / 2
        this.width = (2f * scaleX * width.toFloat()).toInt()  //画布宽度（2倍波长）
        this.path = buildWavePath(width, height)
    }


    private fun buildWavePath(width: Int, height: Int): Path {
        var DP = VasniSchema.instance.dp2px(1f)//一个dp在当前设备表示的像素量（水波的绘制精度设为一个dp单位）
        if (DP < 1) {
            DP = 1
        }

        val wave = (scaleY * this.wave).toInt()//计算拉伸之后的波幅

        val path = Path()
        path.moveTo(0f, 0f)
        path.lineTo(0f, (height - wave).toFloat())

        var xin = DP
        while (xin < width) {
            path.lineTo(
                    xin.toFloat(),
                    height.toFloat() - wave.toFloat() - wave * Math.sin(4.0 * Math.PI * xin.toDouble() / width).toFloat()
            )
            xin += DP
        }

        path.lineTo(width.toFloat(), (height - wave).toFloat())
        path.lineTo(width.toFloat(), 0f)
        path.close()
        return path
    }
}