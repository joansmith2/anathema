package net.sf.anathema.hero.charms.persistence.special;

import net.sf.anathema.hero.charms.model.special.CharmSpecialsModel;

public interface SpecialCharmPersister {

  void saveCharmSpecials(CharmSpecialsModel charmSpecials, CharmSpecialsPto specialsPto);

  void loadCharmSpecials(CharmSpecialsModel charmSpecials, CharmSpecialsPto specialsPto);
}
