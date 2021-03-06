package song.telephone;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

public class DrawingView extends View {

    private Path drawPath;                  //drawing path
    private Paint drawPaint , canvasPaint;  //drawing and canvas paint
    private int paintColor = 0xFFFF0000;    //initial color
    private Canvas drawCanvas;              //canvas
    private Bitmap canvasBitmap;            //canvas bitmap

    private float brushSize , lastBrushSize;

    private boolean erase = false;

    int width , height;

    public DrawingView( Context context , AttributeSet attrs ) {
        super( context , attrs );
        setupDrawing();
    }

    private void setupDrawing() {
        brushSize = getResources().getInteger( R.integer.medium_size );
        lastBrushSize = brushSize;

        drawPath = new Path();
        drawPaint = new Paint();

        drawPaint.setColor( paintColor );

        drawPaint.setAntiAlias( true );
        drawPaint.setStrokeWidth( brushSize );
        drawPaint.setStyle( Paint.Style.STROKE );
        drawPaint.setStrokeJoin( Paint.Join.ROUND );
        drawPaint.setStrokeCap( Paint.Cap.ROUND );

        canvasPaint = new Paint( Paint.DITHER_FLAG );
    }

    @Override
    protected void onSizeChanged( int w , int h , int oldw , int oldh ) {
        super.onSizeChanged( w , h , oldw , oldh );

        width = w;
        height = h;

        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas( canvasBitmap );
    }

    @Override
    protected void onDraw( Canvas canvas ) {
        canvas.drawBitmap( canvasBitmap , 0 , 0 , canvasPaint );
        canvas.drawPath( drawPath , drawPaint );
    }

    @Override
    public boolean onTouchEvent( MotionEvent event ) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch( event.getAction() ) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo( touchX , touchY );
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo( touchX , touchY );
                break;
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath( drawPath , drawPaint );
                drawPath.reset();
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }

    public void setColor( String newColor ) {
        invalidate();
        paintColor = Color.parseColor( newColor );
        drawPaint.setColor( paintColor );
    }

    public void setBrushSize( float newSize ) {
        float pixelAmount = TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP , newSize , getResources().getDisplayMetrics() );
        brushSize = pixelAmount;
        drawPaint.setStrokeWidth( brushSize );
    }

    public void setLastBrushSize( float lastSize ) {
        lastBrushSize = lastSize;
    }

    public float getLastBrushSize() {
        return lastBrushSize;
    }

    public void setErase( boolean isErase ) {
        erase = isErase;
        if( erase ) {
            this.setColor( "#FFFFFFFF" );
//            drawPaint.setXfermode( new PorterDuffXfermode( PorterDuff.Mode.CLEAR ));
        }
        else {
            drawPaint.setXfermode( null );
        }
    }

    public void startNew() {
        drawCanvas.drawColor( 0 , PorterDuff.Mode.CLEAR );
        invalidate();
    }

    public Bitmap getBitmap() {
        return canvasBitmap;
    }

    public void setBitmap( Bitmap newBitmap ) {
        canvasBitmap = newBitmap;
//        drawCanvas.setBitmap( newBitmap );
    }

    public Canvas getCanvas() {
        return drawCanvas;
    }

    public void setCanvas( Canvas newCanvas ) {
        newCanvas.drawColor( Color.RED );
        drawCanvas = newCanvas;
        invalidate();
    }
}
