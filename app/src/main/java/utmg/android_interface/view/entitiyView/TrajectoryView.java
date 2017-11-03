package utmg.android_interface.view.entitiyView;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.List;

import utmg.android_interface.model.util.POI;
import utmg.android_interface.model.util.Point3;
import utmg.android_interface.model.util.Point4;
import utmg.android_interface.model.util.Trajectory;

/**
 * Created by tuckerhaydon on 10/15/17.
 */

/**
 * View class for a trajectory.
 */
public class TrajectoryView extends AbstractEntityView{

    private final Trajectory trajectory;
    private final Paint paint;
    private final POI poi;

    public TrajectoryView(
            final Trajectory trajectory,
            final Paint paint,
            final POI poi) {
        this.trajectory = trajectory;
        this.paint = paint;
        this.poi = poi;
    }

    @Override
    public void draw(
            final Canvas canvas) {
        canvas.drawPath(this.composePath(), this.paint);
    }

    private Path composePath() {
        final List<Point4> pathPoints = this.trajectory.getPoints();

        // If there are no points, return an empty path
        if(pathPoints.size() == 0) {
            return new Path();
        }

        final Path path = new Path();

        // Set the first point in the path
        final Point4 initialPoint = pathPoints.get(0);
        path.moveTo(initialPoint.x, initialPoint.y);

        // Add the rest of the points to the path
        for(int i = 1; i < pathPoints.size(); i++) {
            final Point4 point = pathPoints.get(i);
            path.lineTo(point.x, point.y);
        }

        return path;
    }
}
