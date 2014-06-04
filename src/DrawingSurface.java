//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//import android.graphics.PorterDuff;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//
//
//public class DrawingSurface extends SurfaceView implements SurfaceHolder.Callback {
//  private Bitmap mBitmap;
//  private ...
//  class DrawThread extends  Thread{
//    ...
//    @Override
//    public void run() {
//      Canvas canvas = null;
//      while (_run){
//        try{
//          canvas = mSurfaceHolder.lockCanvas(null);
//          if(mBitmap == null){
//            mBitmap =  Bitmap.createBitmap (1, 1, Bitmap.Config.ARGB_8888);;
//          }
//          final Canvas c = new Canvas (mBitmap);
//          c.drawColor(0, PorterDuff.Mode.CLEAR);
//          commandManager.executeAll(c);
//          canvas.drawBitmap (mBitmap, 0,  0,null);
//        } finally {
//          mSurfaceHolder.unlockCanvasAndPost(canvas);
//        }
//      }
//    }
//  }
//
//  public Bitmap getBitmap(){
//    return mBitmap;
//  }
//
//  public void surfaceChanged(SurfaceHolder holder, int format, int width,  int height) {
//    // TODO Auto-generated method stub
//    mBitmap =  Bitmap.createBitmap (width, height, Bitmap.Config.ARGB_8888);;
//  }
//}