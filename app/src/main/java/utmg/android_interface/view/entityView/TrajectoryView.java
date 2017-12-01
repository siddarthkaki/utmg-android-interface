package utmg.android_interface.view.entityView;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.List;

import utmg.android_interface.model.util.POI;
import utmg.android_interface.model.util.Point3;
import utmg.android_interface.model.util.Point4;
import utmg.android_interface.model.util.Trajectory;
import utmg.android_interface.view.canvas.AbstractCanvas;

/**
 * Created by tuckerhaydon on 10/15/17.
 */

/**
 * View class for a trajectory.
 */
public class TrajectoryView extends AbstractEntityView{

    private final Trajectory trajectory;
    private final Paint paint;
    private final AbstractCanvas drawingCanvas;
    private final POI poi;

    public TrajectoryView(
            final Trajectory trajectory,
            final Paint paint,
            final POI poi,
            final AbstractCanvas drawingCanvas) {
        this.trajectory = trajectory;
        this.paint = paint;
        this.poi = poi;
        this.drawingCanvas = drawingCanvas;
    }

    @Override
    public void draw(
            final Canvas canvas) {
        canvas.drawPath(this.composePath(), this.paint);


        final List<Point4> pathPoints = this.trajectory.getPoints();


        for (int i = 5; i < pathPoints.size(); i = i + 25) {
            Point4 metersP = pathPoints.get(i);
            canvas.drawLine(
                    this.drawingCanvas.toPixelsX(metersP.x),
                    this.drawingCanvas.toPixelsY(metersP.y),
                    (this.poi.x + this.drawingCanvas.toPixelsX(metersP.x)) / 2,
                    (this.poi.y + this.drawingCanvas.toPixelsY(metersP.y)) / 2,
                    paint);

        }
    }

//                this.trajectory.getPoints().get(i).x,
 //               this.trajectory.getPoints().get(i).y,
 //               (this.poi.x+this.trajectory.getPoints().get(i).x)/2,
 //               (this.poi.y+this.trajectory.getPoints().get(i).y)/2,
                //paint);


    private Path composePath() {
        final List<Point4> pathPoints = this.trajectory.getPoints();

        // If there are no points, return an empty path
        if(pathPoints.size() == 0) {
            return new Path();
        }

        final Path path = new Path();

        // Set the first point in the path
        final Point4 initialPointMeters = pathPoints.get(0);
        path.moveTo(this.drawingCanvas.toPixelsX(initialPointMeters.x), this.drawingCanvas.toPixelsY(initialPointMeters.y));

        // Add the rest of the points to the path
        for(int i = 1; i < pathPoints.size(); i++) {
            final Point4 metersPoint = pathPoints.get(i);
            path.lineTo(this.drawingCanvas.toPixelsX(metersPoint.x), this.drawingCanvas.toPixelsY(metersPoint.y));
        }

        return path;
    }
}
