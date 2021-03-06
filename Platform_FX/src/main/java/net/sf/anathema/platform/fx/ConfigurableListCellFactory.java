package net.sf.anathema.platform.fx;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import net.sf.anathema.lib.gui.AgnosticUIConfiguration;

public class ConfigurableListCellFactory<T> implements ListCellFactory<T> {
  private final AgnosticUIConfiguration<T> configuration;

  public ConfigurableListCellFactory(AgnosticUIConfiguration<T> configuration) {
    this.configuration = configuration;
  }

  @Override
  public ListCell<T> call(ListView<T> tListView) {
    return new UITableCell<>(configuration);
  }
}