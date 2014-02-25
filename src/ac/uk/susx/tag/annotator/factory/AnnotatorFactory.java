package ac.uk.susx.tag.annotator.factory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Used to annotate AnnotatorFactory classes in order for automatic class registration to occur.
 * @author jp242
 *
 */
@Target({ElementType.TYPE})
public @interface AnnotatorFactory {}
