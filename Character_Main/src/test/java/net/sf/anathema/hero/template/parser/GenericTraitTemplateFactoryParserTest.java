package net.sf.anathema.hero.template.parser;

import junit.framework.TestCase;
import net.sf.anathema.character.main.traits.ITraitTemplate;
import net.sf.anathema.character.main.traits.types.AttributeType;
import net.sf.anathema.character.main.traits.types.VirtueType;
import net.sf.anathema.character.main.xml.trait.GenericTraitTemplateFactory;
import net.sf.anathema.character.main.xml.trait.GenericTraitTemplateFactoryParser;
import net.sf.anathema.character.main.xml.trait.pool.GenericTraitTemplatePool;
import net.sf.anathema.hero.dummy.template.DummyXmlTemplateRegistry;
import net.sf.anathema.lib.xml.DocumentUtilities;
import org.dom4j.Element;

public class GenericTraitTemplateFactoryParserTest extends TestCase {
  String xml = "<traitTemplates >" + "<virtues>" +
               "<defaultTrait startValue=\"1\" lowerableState=\"RaiseOnly\">" + "<limitation type=\"Static\" value=\"5\"/>" + "<minimum value=\"1\"/>" +
               "</defaultTrait>" + "</virtues>" + "<essence startValue=\"1\" lowerableState=\"RaiseOnly\">" +
               "<limitation type=\"Static\" value=\"1\"/>" + "<minimum value=\"1\"/>" + "</essence>" +
               "<willpower startValue=\"2\" lowerableState=\"RaiseOnly\">" + "<limitation type=\"Static\" value=\"10\"/>" + "<minimum value=\"2\"/>" +
               "</willpower>" + "</traitTemplates>";
  private DummyXmlTemplateRegistry<GenericTraitTemplateFactory> templateFactoryRegistry;
  private GenericTraitTemplateFactoryParser parser;
  private DummyXmlTemplateRegistry<GenericTraitTemplatePool> templatePoolRegistry;

  @Override
  protected void setUp() throws Exception {
    templateFactoryRegistry = new DummyXmlTemplateRegistry<>();
    templatePoolRegistry = new DummyXmlTemplateRegistry<>();
    parser = new GenericTraitTemplateFactoryParser(templateFactoryRegistry, templatePoolRegistry);
  }

  public void testParseEssence() throws Exception {
    Element traitCollectionElement = DocumentUtilities.read(xml).getRootElement();
    GenericTraitTemplateFactory templateFactory = parser.parseTemplate(traitCollectionElement);
    ITraitTemplate essenceTemplate = templateFactory.createEssenceTemplate();
    AnathemaCharacterAssert.assertStaticSimpleTraitTemplate(1, 1, 1, 1, essenceTemplate);
  }

  public void testParseVirtues() throws Exception {
    Element traitCollectionElement = DocumentUtilities.read(xml).getRootElement();
    GenericTraitTemplateFactory templateFactory = parser.parseTemplate(traitCollectionElement);
    ITraitTemplate virtueTemplate = templateFactory.createVirtueTemplate(VirtueType.Compassion);
    AnathemaCharacterAssert.assertStaticSimpleTraitTemplate(1, 1, 1, 5, virtueTemplate);
  }

  public void testParseWillpower() throws Exception {
    Element traitCollectionElement = DocumentUtilities.read(xml).getRootElement();
    GenericTraitTemplateFactory templateFactory = parser.parseTemplate(traitCollectionElement);
    ITraitTemplate willpowerTemplate = templateFactory.createWillpowerTemplate();
    AnathemaCharacterAssert.assertStaticSimpleTraitTemplate(2, 2, 2, 10, willpowerTemplate);
  }
}