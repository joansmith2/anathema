package net.sf.anathema.character.main.framework;

import net.sf.anathema.character.main.magic.parser.charms.ICharmCache;
import net.sf.anathema.character.main.type.ICharacterType;
import net.sf.anathema.character.main.xml.CharacterTemplateParser;
import net.sf.anathema.character.main.xml.GenericCharacterTemplate;
import net.sf.anathema.lib.exception.PersistenceException;
import net.sf.anathema.lib.logging.Logger;
import net.sf.anathema.lib.resources.ResourceFile;

public class CharacterTemplateInitializer {

  private ICharacterType type;

  public CharacterTemplateInitializer(ICharacterType type) {
    this.type = type;
  }

  public void addCharacterTemplates(HeroEnvironment characterGenerics) {
    ICharacterTemplateResourceCache cache = characterGenerics.getDataSet(ICharacterTemplateResourceCache.class);
    for (ResourceFile templateResource : cache.getTemplateResourcesForType(type.getId())) {
      registerParsedTemplate(characterGenerics, templateResource);
    }
  }

  private void registerParsedTemplate(HeroEnvironment generics, ResourceFile resource) {
    ICharacterTemplateRegistryCollection characterTemplateRegistries = generics.getCharacterTemplateRegistries();
    new CharacterTemplateParser(generics.getCharacterTypes(), characterTemplateRegistries, generics.getDataSet(ICharmCache.class));
    try {
      GenericCharacterTemplate template = characterTemplateRegistries.getCharacterTemplateRegistry().get(resource);
      generics.getTemplateRegistry().register(template);
    } catch (PersistenceException e) {
      Logger.getLogger(CharacterTemplateInitializer.class).error(resource.getFileName(), e);
    }
  }
}