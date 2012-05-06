package net.sf.anathema.platform.svgtree.presenter.view;

import java.awt.Cursor;

public interface ISvgTreeViewProperties extends NodeProperties {

  String getToolTip(String nodeId);

  Cursor getCursor(String nodeId);

  Cursor getDefaultCursor();

  Cursor getDragCursor();

  Cursor getForbiddenCursor();

  Cursor getZoomCursor();

  Cursor getControlCursor();
}