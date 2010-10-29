/* 
 * PROJECT: NyARToolkit
 * --------------------------------------------------------------------------------
 * This work is based on the original ARToolKit developed by
 *   Hirokazu Kato
 *   Mark Billinghurst
 *   HITLab, University of Washington, Seattle
 * http://www.hitl.washington.edu/artoolkit/
 *
 * The NyARToolkit is Java edition ARToolKit class library.
 * Copyright (C)2008-2009 Ryo Iizuka
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * For further information please contact.
 *	http://nyatla.jp/nyatoolkit/
 *	<airmail(at)ebony.plala.or.jp> or <nyatla(at)nyatla.jp>
 * 
 */
package jp.nyatla.nyartoolkit.core.transmat;



import jp.nyatla.nyartoolkit.core.types.NyARDoublePoint3d;
import jp.nyatla.nyartoolkit.core.types.matrix.*;

/**
 * NyARTransMat戻り値専用のNyARMat
 * 
 */
public class NyARTransMatResult extends NyARDoubleMatrix44
{
	/**
	 * この行列に1度でも行列をセットしたかを返します。
	 */
	public boolean has_value = false;
	/**
	 * 観測値とのずれを示すエラーレート値です。SetValueにより更新されます。
	 * has_valueがtrueの時に使用可能です。
	 */
	public double last_error;
	/**
	 * コンストラクタです。
	 */
	public NyARTransMatResult()
	{
		this.m30=this.m31=this.m32=0;
		this.m33=1.0;
	}
	/**
	 * ZXY系の角度値を返します。
	 * この関数は、0-PIの間で値を返します。
	 * @param o_out
	 */
	public final void getZXYAngle(NyARDoublePoint3d o_out)
	{
		double sina = this.m21;
		if (sina >= 1.0) {
			o_out.x = Math.PI / 2;
			o_out.y = 0;
			o_out.z = Math.atan2(-this.m10, this.m00);
		} else if (sina <= -1.0) {
			o_out.x = -Math.PI / 2;
			o_out.y = 0;
			o_out.z = Math.atan2(-this.m10, this.m00);
		} else {
			o_out.x = Math.asin(sina);
			o_out.z = Math.atan2(-this.m01, this.m11);
			o_out.y = Math.atan2(-this.m20, this.m22);
		}
	}
	/**
	 * 点を座標変換します。3行目の拡大率は無視します。
	 * @param i_x
	 * @param i_y
	 * @param i_z
	 * @param o_out
	 */
	public final void transformVertex(double i_x,double i_y,double i_z,NyARDoublePoint3d o_out)
	{
		o_out.x=this.m00*i_x+this.m01*i_y+this.m02*i_z+this.m03;
		o_out.y=this.m10*i_x+this.m11*i_y+this.m12*i_z+this.m13;
		o_out.z=this.m20*i_x+this.m21*i_y+this.m22*i_z+this.m23;
		return;
	}
	public final void transformVertex(NyARDoublePoint3d i_in,NyARDoublePoint3d o_out)
	{
		transformVertex(i_in.x,i_in.y,i_in.z,o_out);
	}
	/**
	 * 平行移動量と回転行列をセットします。この関数は、INyARTransmatインタフェイスのクラスが結果を保存するために使います。
	 * @param i_rot
	 * @param i_trans
	 */
	public final void setValue(NyARDoubleMatrix33 i_rot, NyARDoublePoint3d i_trans,double i_error)
	{
		this.m00=i_rot.m00;
		this.m01=i_rot.m01;
		this.m02=i_rot.m02;
		this.m03=i_trans.x;

		this.m10 =i_rot.m10;
		this.m11 =i_rot.m11;
		this.m12 =i_rot.m12;
		this.m13 =i_trans.y;

		this.m20 = i_rot.m20;
		this.m21 = i_rot.m21;
		this.m22 = i_rot.m22;
		this.m23 = i_trans.z;

		this.m30=this.m31=this.m32=0;
		this.m33=1.0;		
		this.has_value = true;
		this.last_error=i_error;
		return;
	}	
}
