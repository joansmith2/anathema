package net.sf.anathema.character.main.persistence.charm;

import net.sf.anathema.character.main.magic.model.charm.Charm;
import net.sf.anathema.character.main.magic.model.charm.special.CharmSpecialsModel;
import net.sf.anathema.character.main.magic.model.charms.ILearningCharmGroup;
import net.sf.anathema.hero.charms.model.CharmsModel;
import net.sf.anathema.lib.util.Identifier;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.sf.anathema.character.main.persistence.ICharacterXmlConstants.ATTRIB_EXPERIENCE_LEARNED;
import static net.sf.anathema.character.main.persistence.ICharacterXmlConstants.ATTRIB_NAME;
import static net.sf.anathema.character.main.persistence.ICharacterXmlConstants.ATTRIB_TYPE;
import static net.sf.anathema.character.main.persistence.ICharacterXmlConstants.TAG_CHARM;
import static net.sf.anathema.character.main.persistence.ICharacterXmlConstants.TAG_CHARMGROUP;
import static net.sf.anathema.character.main.persistence.ICharacterXmlConstants.TAG_SPECIAL;

public class CharmSaver {
  private CharmsModel charmConfiguration;
  private final ISpecialCharmPersister specialPersister;

  public CharmSaver(CharmsModel charmConfiguration) {
    this.charmConfiguration = charmConfiguration;
    this.specialPersister = createSpecialCharmPersister(charmConfiguration);
  }

  private ISpecialCharmPersister createSpecialCharmPersister(CharmsModel charmConfiguration) {
    return new SpecialCharmPersister(charmConfiguration.getSpecialCharms(), charmConfiguration.getCharmIdMap());
  }

  public void saveCharms(Identifier type, Element charmsElement) {
    ILearningCharmGroup[] charmGroups = charmConfiguration.getCharmGroups(type);
    Arrays.sort(charmGroups, new IdentifiedComparator());
    for (ILearningCharmGroup group : charmGroups) {
      if (group.hasLearnedCharms()) {
        saveCharmGroup(charmsElement, group, specialPersister, charmConfiguration);
      }
    }
  }

  private void saveCharmGroup(Element charmsElement, ILearningCharmGroup group, ISpecialCharmPersister specialPersister,
                              CharmsModel charmConfiguration) {
    Element groupElement = charmsElement.addElement(TAG_CHARMGROUP);
    groupElement.addAttribute(ATTRIB_NAME, group.getId());
    groupElement.addAttribute(ATTRIB_TYPE, group.getCharacterType().getId());
    Map<String, Boolean> isExperiencedLearned = getExperiencedLearnedMap(group);
    List<Charm> charms = getSortedCharmList(group);
    for(Charm charm : charms) {
      saveCharm(charmConfiguration, specialPersister, groupElement, charm, isExperiencedLearned.get(charm.getId()));
    }
  }

  private List<Charm> getSortedCharmList(ILearningCharmGroup group) {
    List<Charm> charms = new ArrayList<>();
    for (Charm charm : group.getCreationLearnedCharms()) {
      charms.add(charm);
    }
    for (Charm charm : group.getExperienceLearnedCharms()) {
      charms.add(charm);
    }
    Collections.sort(charms, new IdentifiedComparator());
    return charms;
  }

  private Map<String, Boolean> getExperiencedLearnedMap(ILearningCharmGroup group) {
    HashMap<String, Boolean> isExperiencedLearned = new HashMap<>();
    for (Charm charm : group.getCreationLearnedCharms()) {
      isExperiencedLearned.put(charm.getId(), false);
    }
    for (Charm charm : group.getExperienceLearnedCharms()) {
      isExperiencedLearned.put(charm.getId(), true);
    }
    return isExperiencedLearned;
  }

  private void saveCharm(CharmsModel charmConfiguration, ISpecialCharmPersister specialPersister, Element groupElement, Charm charm,
                         boolean experienceLearned) {
    Element charmElement = groupElement.addElement(TAG_CHARM);
    charmElement.addAttribute(ATTRIB_NAME, charm.getId());
    charmElement.addAttribute(ATTRIB_EXPERIENCE_LEARNED, String.valueOf(experienceLearned));
    CharmSpecialsModel specialCharmConfiguration = charmConfiguration.getCharmSpecialsModel(charm);
    if (specialCharmConfiguration != null) {
      Element specialElement = charmElement.addElement(TAG_SPECIAL);
      specialPersister.saveConfiguration(specialElement, specialCharmConfiguration);
    }
  }
}