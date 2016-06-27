package de.codecentric.centerdevice.javafxsvg;

import javafx.stage.Screen;

import java.lang.reflect.Method;

public class ScreenHelper {

  private static ScreenAccessor accessor = null;

  public static class ScreenAccessor {
    private static Method getRenderScale = null;

    private Method getScaleMethod() throws NoSuchMethodException {
      if (getRenderScale == null) {
        getRenderScale = lookupScaleMethod();
      }

      return getRenderScale;
    }

    private Method lookupScaleMethod() throws NoSuchMethodException {
      Method scaleMethod;
      try {
        scaleMethod = Screen.class.getDeclaredMethod("getScale"); // until 8u51
      } catch (NoSuchMethodException e) {
        scaleMethod = Screen.class.getDeclaredMethod("getRenderScale");
      }
      scaleMethod.setAccessible(true);
      return scaleMethod;
    }

    public float getRenderScale(Screen screen) {
      try {
        Method m = getScaleMethod();
        return ((float) m.invoke(screen));
      } catch (ReflectiveOperationException e) {
        return 1f;
      }
    }
  }

  public static ScreenAccessor getScreenAccessor() {
    if (accessor == null) {
      accessor = new ScreenAccessor();
    }

    return accessor;
  }
}
