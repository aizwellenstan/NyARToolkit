package jp.nyatla.nyartoolkit.core.kpm.matcher.homography_estimation;

import jp.nyatla.nyartoolkit.core.types.NyARDoublePoint2d;
import jp.nyatla.nyartoolkit.core.types.matrix.NyARDoubleMatrix33;

public class HomographyPointsCheck_O1 extends HomographyPointsCheck
{

    /**
     * Check the geometric consistency between three correspondences.
     */
    private static boolean Homography3PointsGeometricallyConsistent(NyARDoublePoint2d x1, NyARDoublePoint2d x2, NyARDoublePoint2d x3,NyARDoublePoint2d x1p, NyARDoublePoint2d x2p, NyARDoublePoint2d x3p)
    {
    	double a=((x2.x-x1.x)*(x3.y-x1.y)-(x2.y-x1.y)*(x3.x-x1.x));
    	double b=((x2p.x-x1p.x)*(x3p.y-x1p.y)-(x2p.y-x1p.y)*(x3p.x-x1p.x));
        if((a>0) ^ (b>0) == true) {
            return false;
        }
        return true;
    } 
    private final NyARDoublePoint2d[] _x=NyARDoublePoint2d.createArray(4);
    private final NyARDoublePoint2d[] _xp=NyARDoublePoint2d.createArray(4);
    
    @Override
    public void setTestWindow(double i_w,double i_h)
    {
    	NyARDoublePoint2d[] x= this._x;
 		x[0].x = 0;
		x[0].y = 0;
		x[1].x = i_w;
		x[1].y = 0;
		x[2].x = i_w;
		x[2].y = i_h;
		x[3].x = 0;
		x[3].y = i_h; 
    }
    
	/**
	 * 4ポイント限定のHomographyPointsGeometricallyConsistent関数
	 * @param H
	 * @param i_width
	 * @param i_height
	 * @return
	 */
    @Override
    public boolean geometricallyConsistent(NyARDoubleMatrix33 H)
    {
    	NyARDoublePoint2d[] x= this._x;
    	NyARDoublePoint2d[] xp=this._xp;  
        
    	//MultiplyPointHomographyInhomogenous
    	for(int i=0;i<4;i++){
    		double sx=x[i].x;
    		double sy=x[i].y;
        	double w = H.m20*sx + H.m21*sy + H.m22;
            xp[i].x = (H.m00*sx + H.m01*sy + H.m02)/w;
            xp[i].y = (H.m10*sx + H.m11*sy + H.m12)/w;
    	}

    	if(!Homography3PointsGeometricallyConsistent(x[0],x[1],x[2],xp[0],xp[1],xp[2])){
            return false;
        }
    	if(!Homography3PointsGeometricallyConsistent(x[1],x[2],x[3],xp[1],xp[2],xp[3])){
            return false;
        }
    	if(!Homography3PointsGeometricallyConsistent(x[2],x[3],x[0],xp[2],xp[3],xp[0])){
            return false;
        }
    	if(!Homography3PointsGeometricallyConsistent(x[3],x[0],x[1],xp[3],xp[0],xp[1])){
            return false;
        }
        return true;
    }

}