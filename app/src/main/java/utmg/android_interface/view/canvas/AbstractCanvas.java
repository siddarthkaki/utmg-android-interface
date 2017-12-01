package utmg.android_interface.view.canvas;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

/***
 * Created by James on 7/27/2017. Modified by Tucker Haydon on 10/15/2017.
 */
public abstract class AbstractCanvas extends View {
    private Bitmap bitmap;
    protected Canvas canvas;
    private final SharedPreferences preferences;

    private static final int DEFAULT_WIDTH_PIXELS = 1000;
    private static final int DEFAULT_HEIGHT_PIXELS = 1000;

    private static final float DEFAULT_WIDTH_METERS = 1.0f;
    private static final float DEFAULT_LENGTH_METERS = 1.0f;

    private float arenaLength;
    private float arenaWidth;
    private float canvasLength;
    private float canvasWidth;

    public AbstractCanvas(
            final Context context) {
        super(context);
        this.preferences = context.getSharedPreferences("Pref", 0);
        this.bitmap = Bitmap.createBitmap(DEFAULT_WIDTH_PIXELS, DEFAULT_HEIGHT_PIXELS, Bitmap.Config.ARGB_8888);
        this.canvas = new Canvas(this.bitmap);
        this.arenaWidth = preferences.getFloat("arenaWidthMeters", DEFAULT_WIDTH_METERS);
        this.arenaLength = preferences.getFloat("arenaLengthMeters", DEFAULT_LENGTH_METERS);
        this.canvasWidth = this.bitmap.getWidth();
        this.canvasLength = this.bitmap.getHeight();
    }

    /**
     * Maps an x-coordinate from meters coordinate space to pixels coordinate space
     * @param x The x coordinate in meters
     * @return The x coordinate in pixels
     */
    public final float toPixelsX(
            final float x) {
        return (canvasWidth/arenaWidth) * (x + 0.5f * arenaWidth);
    }

    /**
     * Maps a y-coordinate from meters coordinate space to pixels coordinate space
     * @param y The y coordinate in meters
     * @return The y coordinate in pixels
     */
    public final float toPixelsY(
            final float y) {
        return (-canvasLength/arenaLength) * (y - 0.5f * arenaLength);
    }

    /**
     * Maps an x-coordinate from pixels coordinate space to meters coordinate space
     * @param x The x coordinate in pixels
     * @return The x coordinate in meters
     */
    public final float toMetersX(
            final float x) {
        return x * (arenaWidth/canvasWidth) - 0.5f * arenaWidth;
    }

    /**
     * Maps a y-coordinate from pixels coordinate space to meters coordinate space.
     * @param y The y coordinate in pixels
     * @return The y coordinate in meters
     */
    public final float toMetersY(
            final float y) {
        return y * (-arenaLength/canvasLength) + 0.5f * arenaLength;
    }

    /**
     * This is called when the canvas size changes, perhaps due to a screen rotation
     *
     * @param newWidth  The new canvas width
     * @param newHeight The new canvas height
     * @param oldWidth  The old canvas width
     * @param oldHeight The old canvas height
     */
    @Override
    protected final void onSizeChanged(
            final int newWidth,
            final int newHeight,
            final int oldWidth,
            final int oldHeight) {
        super.onSizeChanged(newWidth, newHeight, oldWidth, oldHeight);

        // your Canvas will draw onto the defined Bitmap
        this.bitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
        this.canvas = new Canvas(bitmap);

        // Update the canvas width and height
        this.canvasWidth = this.bitmap.getWidth();
        this.canvasLength = this.bitmap.getHeight();
    }

    /**
     * This is called when the view should render its content.
     *
     * @param canvas The canvas object to render the content to.
     */
    @Override
    protected void onDraw(
            final Canvas canvas) {
        super.onDraw(canvas);
        this.drawGridlines(canvas);
    }

    private void drawGridlines(
            final Canvas canvas) {
        final Paint paint = new Paint();

        /* Draw an extra bold line for the origin lines */
        paint.setStrokeWidth(4);
        paint.setColor(Color.BLACK);

        // Draw a line above the origin
        canvas.drawLine(0, toPixelsY(0), canvasWidth, toPixelsY(0), paint);

        // Draw a line below the origin
        canvas.drawLine(0, toPixelsY(0), canvasWidth, toPixelsY(0), paint);

        // Draw a line right of the origin
        canvas.drawLine(toPixelsX(0), 0, toPixelsX(0), canvasLength, paint);

        // Draw a line left of the origin
        canvas.drawLine(toPixelsX(0), 0, toPixelsX(0), canvasLength, paint);


        /* Draw lighter lines for the non-origin line */
        paint.setStrokeWidth(2);
        paint.setColor(Color.GRAY);

        // Draw horizontal lines every meter
        for(int i = 1; i < arenaLength/2; i++) {
            // Draw a line above the origin
            canvas.drawLine(0, toPixelsY(i), canvasWidth, toPixelsY(i), paint);

            // Draw a line below the origin
            canvas.drawLine(0, toPixelsY(-i), canvasWidth, toPixelsY(-i), paint);
        }

        // Draw vertical lines every meter
        for(int i = 1; i < arenaWidth/2; i++) {
            // Draw a line right of the origin
            canvas.drawLine(toPixelsX(i), 0, toPixelsX(i), canvasLength, paint);

            // Draw a line left of the origin
            canvas.drawLine(toPixelsX(-i), 0, toPixelsX(-i), canvasLength, paint);
        }
    }
}
